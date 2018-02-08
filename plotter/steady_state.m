clc; close all; clear;

feature = 'RTCreationSize';
prefix = 'create_f';
xlbl = 'Record Size';
ylbl = 'Creation time [ms]';
scaling = 0.001;
ymax = 16;

%feature = 'RTAccessSize';
%prefix = 'access_f';
%xlbl = 'Record Size';
%ylbl = 'Access time [ns]';
%scaling = 1;
%ymax = 2000;
%ymax = 50;

%feature = 'RTAccessFields';
%prefix = 'access_f';
%xlbl = 'Field index';
%ylbl = 'Access time [ns]';
%scaling = 1;
%ymax = 50;

%feature = 'RTAccessPolymorphism';
%prefix = 'poly_deg';
%xlbl = 'Degree of Polymorphism';
%ylbl = 'Access time [ns]';
%scaling = 1;
%ymax = 2500;
%ymax = 45;

%feature = 'RTUpdateSize';
%prefix = 'update_f';
%xlbl = 'Record Size';
%ylbl = 'Update time [ns]';
%scaling = 1;
%ymax = 500;

pigs = [
   cellstr('scala212_caseclass'), cellstr('Scala 2.12 Case class');
   cellstr('scala212_compossible'), cellstr('Scala 2.12 Compossible');
];

colors = [ 
    0 0 0;      %black
    1 0.7 0.1   %orange
    0 0.8 0.8;  %heaven
    1 0 0;      %red
    0.2 0.8 0.2;%green
    0 0 0;      %black
    
    
    
    0 0 1;      %blue
    0.1 0.8 1;  %cyan
    1 0 1;      % magenta
    
    %0.4 0 0.4;  % dark lila
    %0.5 0.3 0.1;  % brown
 ];

k = 3;
covthresh = 0.02;
confidence = 0.95;

mpl = figure(); hold on;
plots = [];

for pigindex = 1:length(pigs)
    pig = pigs{pigindex};
    disp(pig);
    filename = ['../../../data/',pig,'/',feature,'.json'];
    text = fileread(filename);
    
    benchmarks = jsondecode(text);

    %num_forks = benchmarks(1).forks;

    inputs = [];
    meantimes = [];
    errors = [];
    rawtimes = [];

    for bindex = 1:length(benchmarks)
        data = benchmarks(bindex);
        rexp = ['(\w+).(\w+).',prefix,'(\d+)'];
        fieldcell = regexp(data.benchmark, rexp ,'tokens');
        params = fieldcell{1};
        pkg = params{1};
        methodname = params{2};
        input = str2num(params{3});

        X = data.primaryMetric.rawData .* scaling; % each row is an invokation
        %X = [1 ; 5; 10] * [ 123 1234 14235 123 123 321 213 212 272 212 242 212 222 232 212 212 210];

        
        [n,q] = size(X); % n is the number of forks, q is the maximum number of measurements
        wM = movmean(X,k,2,'EndPoints','discard');
        wS = movstd(X,k,0,2,'EndPoints','discard');
        wCoV = wS ./ wM;

        avgs = [];
        %figure();
        for findex = 1:n
            xs = X(findex,:);
            covs = wCoV(findex,:);
            fst = find(covs <= covthresh,1);
            if (numel(fst) == 0)
                disp(['STEADY STATE NOT REACHED FOR INPUT ',num2str(input), ', FORK ',num2str(findex)]);
                fst = length(covs);
            end
            
            %subplot(2,n,findex);
            %plot(1:q, xs);
            %axis([0,q,0,mean(xs)*10]);
            %subplot(2,n,findex+n); hold on;
            %plot(k:q, wCoV(findex,:));
            %plot([0,q],[covthresh, covthresh]);
            %plot([fst+k-1, fst+k-1],[0, max(covs)]);
            %axis([0,q,0,0.2]);
            avgs = [avgs; wM(findex, fst)];
        end
        %%%%%%%%%%%%%%%%
        num_forks = n;
        m = mean(avgs);
        s = std(avgs); % n-1 weighting by default

        z = tinv(1-(1-confidence)/2,n-1); %  confidence interval if n < 30 use student's t distr
        e = z * s / sqrt(n);

        % Collect measurements in arrays
        inputs = [inputs; input];
        meantimes = [meantimes; m];
        errors = [errors; e];
        rawtimes = [rawtimes; avgs'];
        
        % Validate: compare our Error estimate with JMH estimate
        %disp(e);
        %disp(data.primaryMetric.scoreError);
    end

    % Sort according to input
    [f, indices] = sort(inputs);
    m = meantimes(indices);
    e = errors(indices);
    t = rawtimes(indices,:);

    % Plot
    figure(mpl);
    color = colors(pigindex,:);
    p = plot_ci(f,[m,m-e,m+e],'PatchColor', color, 'PatchAlpha', 0.1, 'MainLineWidth', 1, 'MainLineStyle', '-', 'MainLineColor', color,'LineWidth', 1, 'LineStyle','--', 'LineColor', color);
    plots = [plots p.Plot];
    %plot(f,m,'Color',color)
    
    if (strcmp(pig,'javamethodreflection__java_1_8'))
        f_scatter = repelem(f, num_forks);
        m_scatter = reshape(t', [length(f)*num_forks,1]);
        scatter(f_scatter, m_scatter,'x', 'LineWidth', 1,'MarkerEdgeColor',color);
    end
end
axis([min(inputs) max(inputs) 0 ymax]);
xlabel(xlbl);
ylabel(ylbl);
legend(plots, pigs(:,2), 'Location','northwest');