save_hs();

function printphoto(tri, lon, lat, data, range, name)

figID=figure;
set(figID,'Units', 'Normalize', 'Position',[0 0 1 1],'Menu','none', ...
          'ToolBar','none','resize','off','visible','off','Color', 'none');
ax=axes; hold on;

trisurf(tri,lon,lat,data(:,:)); axis image; shading flat;

colormap('jet'); caxis(range);
ax.XAxis.Visible='off'; ax.YAxis.Visible='off';
exportgraphics(figID,'buff.jpg');
%close all;
%imwrite(imcrop(imread('buff.jpg'),[4 4 775 915]), name);
end

function save_hs()

baseName = 'schout_';
prefix = '.nc';

disp('HERE WE GO')
for n = 1:3
    nom = strcat(baseName,int2str(n),prefix);
    disp(strcat(nom, int2str(n)));
    A=ncinfo(nom);
    lon=ncread(nom,'SCHISM_hgrid_node_x');
    lat=ncread(nom,'SCHISM_hgrid_node_y');
    tri=ncread(nom,'SCHISM_hgrid_face_nodes')'; tri=tri(:,1:3);
    Hs=ncread(nom,'WWM_1');
    lim_i = size(Hs,2);

    for i = 1:lim_i
        disp(i);
        printphoto(tri, lon, lat, Hs(:,i), [0 8], strcat('HS_', int2str(n), '_', int2str(i), '.png'))
        
        %ax.XLim = [72,76]; %Rango de las X
        %ax.YLim = [-2,8]; %Rango de las Y
    end
end
disp('dooOOONEEE');
end


function crear_video(path_imagenes,nombre_video,fpseg)

%--- me cojo las imágenes
imagenes=dir([path_imagenes,'*.jpg']); imagenes={imagenes.name}';

%--- saco el numero de frame
inn=cellfun(@(x)find(ismember(x,'_')==1,1,'last')+1,imagenes,'UniformOutput',0);
fin=cellfun(@(x)find(ismember(x,'.')==1,1,'last')-1,imagenes,'UniformOutput',0);
nf=cellfun(@(x,inn,fin)str2double(x(inn:fin)),imagenes,inn,fin,'UniformOutput',1);

%--- los ordeno
[nf,ix]=sort(nf); imagenes=imagenes(ix);

%--- abro el video con las características
outputVideo = VideoWriter(nombre_video,'MPEG-4'); outputVideo.FrameRate=fpseg;
open(outputVideo);

%--- voy poninendo los frames
total=length(imagenes)
for nim = 1:total
    disp([num2str(nim),' de ',num2str(total)]);
    img = imread([path_imagenes,imagenes{nim}]); writeVideo(outputVideo,img);
end
close(outputVideo);

end



