clc; close all; clear;

feature = 'CTCreationAccessSize';
prefix = 'compile_s';
xlbl = 'Record Size';
ylbl = 'Compilation time [s]';
scaling = 1;
ymax = 45;


pigs = [
    %cellstr('scalarecords_0_3__scala_2_11_8');   
    cellstr('caseclass__scala_2_11_8'), cellstr('Case Class');            
    cellstr('anonrefinements__scala_2_11_8'), cellstr('Anon. Refinements'); 
    cellstr('scalarecords_0_4__scala_2_11_8'), cellstr('scala-records 0.4');     
    cellstr('compossible_0_2__scala_2_11_8'), cellstr('Compossible 0.2');     
    cellstr('shapeless_2_3_2__scala_2_11_8'), cellstr('Shapeless 2.3.2');    
    %cellstr('shapeless_2_3_0__scala_2_11_8'), cellstr('Shapeless 2.3.0');    
    %cellstr('shapeless_2_2_5__scala_2_11_8'), cellstr('Shapeless 2.2.5');     
     
    
    %cellstr('caseclass__dotty_0_1');               %magenta
    %cellstr('whiteoaknative__whiteoak_2_1');       %yellow
    %cellstr('selreclist__dotty_0_1');              %orange
];

colors = [
    0 0 0; %black
    1 0 0; %red
    0.2 0.8 0.2;  %green
    0 0 1; %blue
    0.1 0.8 1; %cyan
    1 0 1; %magenta
    %0 0 0; %black
    
    
   
    
    %1 1 0; %yellow
    

    %1 0.5 0       %orange
    %0.5 0.3 0.1;  % brown
 ];

plots = [];
mpl = figure(); %hold on;

[num_pigs, ~] = size(pigs);

for pigindex = 1:num_pigs
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
        
        [n,q] = size(X); % n is the number of forks, q is the number of iterations (should be 1!)

        num_forks = n;
        m = mean(X);
        s = std(X);  % n-1 weighting by default

        z = tinv(1-(1-0.99)/2,n-1); %  99% confidence interval if n < 30 use student's t distr
        e = z * s / sqrt(n);

        % Collect measurements in arrays
        inputs = [inputs; input];
        meantimes = [meantimes; m];
        errors = [errors; e];
        disp(X);
        rawtimes = [rawtimes; X'];
        
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
    figure(mpl); hold on;
    color = colors(pigindex,:);
    %p = semilogy(f,m); hold on;
    %plots = [plots p];
    p = plot_ci(f,[m,m-e,m+e],'PatchColor', color, 'PatchAlpha', 0.1, 'MainLineWidth', 1, 'MainLineStyle', '-', 'MainLineColor', color,'LineWidth', 1, 'LineStyle','--', 'LineColor', color);
    plots = [plots p.Plot];
    
    %f_scatter = repelem(f, num_forks);
    %m_scatter = reshape(t', [length(f)*num_forks,1]);
    %scatter(f_scatter, m_scatter,'x', 'LineWidth', 1,'MarkerEdgeColor',color);
end
%fp = f(1:end-2);
%mp = m(1:end-2);

%Xp = [fp.^2 fp ones(length(fp), 1)];
%c = Xp \ mp;

%X = [f.^2 f ones(length(f), 1)];
%p = plot(f, X*c,'k:','LineWidth',2);
%plots = [plots, p];

%mlog = log(m);
%X = [f ones(length(f), 1)];
%c = X \ mlog;
%X = [f ones(length(f), 1)];
%p = plot(f, exp(X*c),'k--','LineWidth',2);
%plots = [plots, p];

axis([min(inputs) max(inputs) 0 ymax]);
xlabel(xlbl);
ylabel(ylbl);
%legend(plots, [pigs(:,2); cellstr('quadratic'); cellstr('exponential')], 'Location','northwest');
legend(plots, pigs(:,2), 'Location','northwest');