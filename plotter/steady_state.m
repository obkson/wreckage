clc; close all; clear;

%feature = 'RTAccessPolymorphism';
%prefix = 'poly_deg';

%feature = 'RTAccessFields';
%prefix = 'access_f';

feature = 'RTCreationFields';
prefix = 'create_f';

pigs = [
    %cellstr('scalarecords_0_3__scala_2_11_8');    %black
    %cellstr('scalarecords_0_4__scala_2_11_8');     %blue
    %cellstr('shapeless_2_3_2__scala_2_11_8');      %green
    cellstr('compossible_0_2__scala_2_11_8');      %cyan
    %cellstr('caseclass__scala_2_11_8');            %red
    %cellstr('caseclass__dotty_0_1');               %magenta
    %cellstr('whiteoaknative__whiteoak_2_1');       %yellow
    %cellstr('selreclist__dotty_0_1');              %orange
];
colors = [
    %0 0 0; %black
    0 0 1; %blue
    %0 1 0; %green
    %0 1 1; %cyan
    %1 0 0; %red
    %1 0 1; %magenta
    %1 1 0; %yellow
    %1 0.5 0       %orange
    %0.5 0.3 0.1;  % brown
 ];

k = 10;
covthresh = 0.02;

figure(1); hold on;

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

        X = data.primaryMetric.rawData; % each row is an invokation
        %X = [1 ; 5; 10] * [ 123 1234 14235 123 123 321 213 212 272 212 242 212 222 232 212 212 210];
        
        [n,q] = size(X); % n is the number of forks, q is the maximum number of measurements
        wM = movmean(X,k,2,'EndPoints','discard');
        wS = movstd(X,k,0,2,'EndPoints','discard');
        wCoV = wS ./ wM;

        avgs = [];
        figure();
        for findex = 1:n
            xs = X(findex,:);
            covs = wCoV(findex,:);
            fst = find(covs <= covthresh,1);
            if (numel(fst) == 0)
                disp(['STEADY STATE NOT REACHED FOR INPUT ',num2str(input), ', FORK ',num2str(findex)]);
                fst = length(covs);
            end
            
            subplot(2,n,findex);
            plot(1:q, xs);
            axis([0,q,0,mean(xs)*10]);
            subplot(2,n,findex+n); hold on;
            plot(k:q, wCoV(findex,:));
            plot([0,q],[covthresh, covthresh]);
            plot([fst+k-1, fst+k-1],[0, max(covs)]);
            axis([0,q,0,0.2]);
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
    figure(1);
    color = colors(pigindex,:);
    plot_ci(f,[m,m-e,m+e],'PatchColor', color, 'PatchAlpha', 0.1, 'MainLineWidth', 1, 'MainLineStyle', '-', 'MainLineColor', color,'LineWidth', 1, 'LineStyle','--', 'LineColor', 'k');
    %plot(f,m,'Color',color)
    %axis([1 10 0 25]);
    f_scatter = repelem(f, num_forks);
    m_scatter = reshape(t', [length(f)*num_forks,1]);
    scatter(f_scatter, m_scatter,'x', 'LineWidth', 1,'MarkerEdgeColor',color);
end