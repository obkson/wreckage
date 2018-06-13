clc; close all; clear;

pigs = [
    cellstr('dotty08_records'), cellstr('Dotty 0.8 Records');
    cellstr('dotty08_caseclass'), cellstr('Dotty 0.8 Case class');
    cellstr('scala212_compossible'), cellstr('Scala 2.12 Compossible 0.2');
    cellstr('scala212_shapeless233'), cellstr('Scala 2.12 Shapeless 2.3.3');
];
scaling=0.001;

covthresh = 0.02;
confidence = 0.95;

ms = [];
es = [];

for pigindex = 1:length(pigs)
        
    pig = pigs{pigindex};
    
    disp(pig);
    filename = ['../../../data/',pig,'/','RTCaseStudyRead','.json'];
    text = fileread(filename);
    
    benchmarks = jsondecode(text);
    
    %num_forks = benchmarks(1).forks;

    data = benchmarks(1);
    rexp = ['(\w+).(\w+).','calc_stats'];
    fieldcell = regexp(data.benchmark, rexp ,'tokens');
    params = fieldcell{1};
    pkg = params{1};
    methodname = params{2};

    X = data.primaryMetric.rawData .* scaling; % each row is an invokation
    
    [n,q] = size(X); % n is the number of forks, q is the maximum number of measurements
    
    m = mean(X);
    s = std(X);  % n-1 weighting by default

    z = tinv(1-(1-confidence)/2,n-1); %  confidence interval if n < 30 use student's t distr
    e = z * s / sqrt(n);
    
    % Collect measurements in arrays
    ms = [ms; m];
    es = [es; e];
end

figure;
hold on;
bar(1:length(pigs),ms);
errorbar(1:length(pigs), ms, es, '.', 'LineWidth', 1, 'Color', [0,0,0]);
set(gca,'xTick', 1:length(pigs), 'xticklabel', pigs(:,2));
ylabel('Running time [s]')