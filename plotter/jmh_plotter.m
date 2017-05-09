clc; close all; clear;

%feature = 'RTAccessPolymorphism';
%prefix = 'poly_deg';


feature = 'RTAccessFields';
prefix = 'access_f';

pigs = [
    %cellstr('scalarecords_0_3__scala_2_11_8');    %black
    cellstr('scalarecords_0_4__scala_2_11_8');     %blue
    cellstr('shapeless_2_3_2__scala_2_11_8');      %green
    cellstr('whiteoaknative__whiteoak_2_1');   %red
    cellstr('caseclass__scala_2_11_8');            %magenta
    cellstr('caseclass__dotty_0_1');            %yellow
    cellstr('selreclist__dotty_0_1');            %brown
];
colors = [
    %0 0 0; %black
    0 0 1; %blue
    0 1 0; %green
    %0 1 1; %cyan
    1 0 0; %red
    1 0 1; %magenta
    1 1 0; %yellow
    0.5 0.3 0.1; % brown
 ];

figure(); hold on;
for pigindex = 1:length(pigs)
    pig = pigs{pigindex};
    filename = ['../../../data/',pig,'/',feature,'.json'];
    text = fileread(filename);
    
    benchmarks = jsondecode(text);

    num_forks = benchmarks(1).forks;

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

        times = data.primaryMetric.rawData; % each row is an invokation
        
        % Add all measurements accross the forks
        % DISCUSS THIS WITH PHALLER!!
        [r,c] = size(times);
        avgs = reshape(times, [r*c , 1]);
        num_forks = r*c;
        
        n = length(avgs);
        m = mean(avgs);
        s = std(avgs); % n-1 weighting by default

        z = tinv(1-(1-0.999)/2,n-1); %  99.9% confidence interval if n < 30 use student's t distr
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
    color = colors(pigindex,:);
    plot_ci(f,[m,m-e,m+e],'PatchColor', color, 'PatchAlpha', 0.1, 'MainLineWidth', 1, 'MainLineStyle', '-', 'MainLineColor', color,'LineWidth', 1, 'LineStyle','--', 'LineColor', 'k');
    %plot(f,m,'Color',color)
    %axis([1 10 0 25]);
    f_scatter = repelem(f, num_forks);
    m_scatter = reshape(t', [length(f)*num_forks,1]);
    scatter(f_scatter, m_scatter,'x', 'LineWidth', 1,'MarkerEdgeColor',color);
end