
nomU = "UIB_U10MET_2021-05-12.nc";
nomV = "UIB_V10MET_2021-05-12.nc";
A=ncinfo(nomU);
lon=ncread(nomU,'lon');
lat=ncread(nomU,'lat');
U10MET=ncread(nomU,'U10MET');
V10MET=ncread(nomV,'V10MET');

%quiver(lon(100:110,100:110),lat(100:110,100:110),U10MET(100:110,100:110,1),V10MET(100:110,100:110,1));
%save_psl();
save_wind();

function printphoto(lon, lat, data, range, name);

figID=figure;
set(figID,'Units', 'Normalize', 'Position',[0 0 1 1],'Menu','none', ...
          'ToolBar','none','resize','off','visible','off');
ax=axes; hold on;

surf(lon,lat,data(:,:)); axis image; shading flat;

colormap('jet'); caxis(range);
ax.XAxis.Visible='off'; ax.YAxis.Visible='off';
%exportgraphics(figID, 'buff.jpg',"BackgroundColor","red",'Resolution',600);
exportgraphics(figID, name,"BackgroundColor","red");
close all;
%imwrite(imcrop(imread("buff.jpg"),[4 4 775 915]), name);

end


function save_psl();

baseName = 'UIB_PSL_2021-05-';
prefix = '.nc';

disp('HERE WE GO')
for n = 1:3
    nom = strcat(baseName,int2str(11+n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    lon=ncread(nom,'lon');
    lat=ncread(nom,'lat');
    PSL=ncread(nom,'PSL');
    lim_i = size(PSL,3);

    for i = 1:lim_i
        disp(i);
        printphoto(lon,lat,PSL(:,:,i),[995 1025],strcat('PSL_',int2str(n),'_',int2str(i),'.jpg'));
    end
end
disp('dooOOONEEE');
end

function RWIND = calc_wind(U10MET, V10MET);
RWIND = zeros(size(U10MET,1),size(U10MET,2),size(U10MET,3));
for i = 1:size(RWIND,3);
    RWIND(:,:,i) = sqrt((U10MET(:,:,i).^2) + (V10MET(:,:,i).^2));
    RWIND(:,:,i) = RWIND(:,:,i) * 1.94384;
end
end

function save_wind();

baseNameU = 'UIB_U10MET_2021-05-';
baseNameV = 'UIB_V10MET_2021-05-';
prefix = '.nc';

disp('HERE WE GO');
for n = 1:3
    nom = strcat(baseNameU,int2str(11+n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    lon=ncread(nom,'lon');
    lat=ncread(nom,'lat');
    U10MET=ncread(nom,'U10MET');
    
    nom = strcat(baseNameV,int2str(11+n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    V10MET=ncread(nom,'V10MET');

    RWIND = calc_wind(U10MET,V10MET);
    lim_i = size(RWIND,3);
    for i = 1:lim_i
        disp(i);
        printphoto(lon,lat,RWIND(:,:,i),[0 40],strcat('WIND_',int2str(n),'_',int2str(i),'.jpg'));
    end
end
disp('dooOOONEEE');
end