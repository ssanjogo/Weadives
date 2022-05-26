


function RWIND = calc_wind(U10MET, V10MET)
RWIND = zeros(size(U10MET,1),size(U10MET,2),size(U10MET,3));
for i = 1:size(RWIND,3)
    tempU = U10MET(:,:,i);
    tempV = V10MET(:,:,i);
    RWIND(:,:,i) = sqrt(tempU.^2 + tempV.^2) .* 1,94384;
end
end




function save_gif()

baseNameU = 'UIB_U10MET_2021-05-';
baseNameV = 'UIB_V10MET_2021-05-';
prefix = '.nc';

disp('HERE WE GO')
for n = 1:3
    nom = strcat(baseName,int2str(11+n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    lon=ncread(nom,'lon');
    lat=ncread(nom,'lat');
    U10MET=ncread(nom,'U10MET');
    
    nom = strcat(baseName,int2str(11+n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    V10MET=ncread(nom,'V10MET');
    
    lim_i = size(U10MET,2);

    R_wind = calc_wind(U10MET,V10MET);

    for i = 1:lim_i
        disp(i);
        figID=figure;
        set(figID,'units','centimeters','Position',[-20 1.5 17.5 20],'color','w',...
            'resize','off','PaperPositionMode','auto','PaperType','A0','visible','off',...
            'InvertHardCopy','off');
        ax=axes; hold on;
        surf(lon,lat,R_wind(:,:,i)); axis image; shading flat;
        colormap('jet'); caxis([0 30]);

        saveas(figID, strcat('PSL_', int2str(n), '_', int2str(i-1), '.jpg'));
    end
end
disp('dooOOONEEE');
end