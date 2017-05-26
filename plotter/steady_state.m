clc; close all; clear;

%feature = 'RTAccessPolymorphism';
%prefix = 'poly_deg';
%xlbl = 'Degree of Polymorphism';
%ylbl = 'Access time [ns]';
%scaling = 1;
%ymax = 60;

feature = 'RTAccessSize';
prefix = 'access_f';
xlbl = 'Record Size';
ylbl = 'Access time [ns]';
scaling = 1;
ymax = 250;

%feature = 'RTAccessFields';
%prefix = 'access_f';
%xlbl = 'Field index';
%ylbl = 'Access time [ns]';
%scaling = 1;
%ymax = 240;

%feature = 'RTCreationFields';
%prefix = 'create_f';
%xlbl = 'Number of Fields';
%ylbl = 'Creation time [ms]';
%scaling = 0.001;
%ymax = 9;

pigs = [
    %cellstr('scalarecords_0_3__scala_2_11_8');   
    cellstr('javafieldreflection__java_1_8'), cellstr('Field Reflection');
    cellstr('javamethodreflection__java_1_8'), cellstr('Method Reflection');
    cellstr('interfacerecord__scala_2_11_8'), cellstr('Interface per Field');
    cellstr('arrayrecord__scala_2_11_8'), cellstr('Array');
    cellstr('listrecord__scala_2_11_8'), cellstr('List');
    cellstr('hashmaprecord__scala_2_11_8'), cellstr('HashMap');
    cellstr('caseclass__scala_2_11_8'), cellstr('Case Class');            
    cellstr('anonrefinements__scala_2_11_8'), cellstr('Anon. Refinements'); 
    %cellstr('scalarecords_0_4__scala_2_11_8'), cellstr('scala-records 0.4');     
    %cellstr('compossible_0_2__scala_2_11_8'), cellstr('Compossible 0.2');     
    %cellstr('shapeless_2_3_2__scala_2_11_8'), cellstr('Shapeless 2.3.2');     
    
    %cellstr('caseclass__dotty_0_1');               %magenta
    %cellstr('whiteoaknative__whiteoak_2_1');       %yellow
    %cellstr('selreclist__dotty_0_1');              %orange
];

colors = [
    0 0 0; %black
    1 0 0; %red
    0 1 0; %green
    0 0 1; %blue
    0 1 1; %cyan
    1 0 1; %magenta
    1 1 0; %yellow
    1 0.5 0       %orange
    0.5 0.3 0.1;  % brown
 ];

k = 10;
covthresh = 0.02;

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

        z = tinv(1-(1-0.99)/2,n-1); %  99% confidence interval if n < 30 use student's t distr
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
    p = plot_ci(f,[m,m-e,m+e],'PatchColor', color, 'PatchAlpha', 0.1, 'MainLineWidth', 1, 'MainLineStyle', '-', 'MainLineColor', color,'LineWidth', 1, 'LineStyle','--', 'LineColor', 'k');
    plots = [plots p.Plot];
    %plot(f,m,'Color',color)
    
    %f_scatter = repelem(f, num_forks);
    %m_scatter = reshape(t', [length(f)*num_forks,1]);
    %scatter(f_scatter, m_scatter,'x', 'LineWidth', 1,'MarkerEdgeColor',color);
end
axis([min(inputs) max(inputs) 0 ymax]);
xlabel(xlbl);
ylabel(ylbl);
legend(plots, pigs(:,2), 'Location','northwest');