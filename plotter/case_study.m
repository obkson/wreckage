clc; close all; clear;

pigs = [
   cellstr('compossible_0_2__scala_2_12_3'), cellstr('Scala2 Compossible');
   cellstr('records__dotty_0_6_snapshot'), cellstr('Dotty0.6 Records');
   cellstr('recordsdirect__dotty_0_6_snapshot'), cellstr('Dotty0.6 Records Direct');
   cellstr('recordsunsafe__dotty_0_6_snapshot'), cellstr('Dotty0.6 Records Unsafe');
   cellstr('caseclass__dotty_0_4_snapshot'), cellstr('Dotty Case classes');
   cellstr('caseclass__scala_2_12_3'), cellstr('Scala2 Case classes');
   cellstr('shapeless_2_3_2__scala_2_12_3'), cellstr('Scala2 Shapeless')
];
scaling=1;

k = 5;
covthresh = 0.02;
confidence = 0.95;

ms = [];
es = [];

for pigindex = 1:length(pigs)
    pig = pigs{pigindex};
    disp(pig);
    filename = ['../../../data/',pig,'/','RTCaseStudy','.json'];
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
            disp(['STEADY STATE NOT REACHED FOR FORK ',num2str(findex)]);
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
    ms = [ms; m];
    es = [es; e];
end

figure;
hold on;
bar(1:length(pigs),ms);
errorbar(1:length(pigs), ms, es, '.', 'LineWidth', 1, 'Color', [0,0,0]);
set(gca,'xTick', 1:length(pigs), 'xticklabel', pigs(:,2));