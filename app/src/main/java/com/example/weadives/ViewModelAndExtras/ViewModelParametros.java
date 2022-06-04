package com.example.weadives.ViewModelAndExtras;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;

import com.example.weadives.Model.DatoGradosClass;
import com.example.weadives.Model.Directions;
import com.example.weadives.Model.ParametrosClass;
import com.example.weadives.Model.PublicacionClass;
import com.example.weadives.Model.UserClass;
import com.example.weadives.Model.MarcadorClass;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ViewModelParametros implements DatabaseAdapter.vmpInterface, DatabaseAdapter.preferenciasInterface{
    private static ViewModelParametros SINGLETON_INSTANCE;
    private  MutableLiveData<ArrayList<ParametrosClass>> mutableList;
    private final DatabaseAdapter dbA;
    private MutableLiveData<String> idNotification;
    public Context c;
    public Resources r;
    private UserClass currentUser;
    private ParametrosClass currentParametro;
    private MarcadorClass marcador;
    //private MutableLiveData<ArrayList<ParametrosClass>> mutableList;

    private ArrayList<ParametrosClass> lista;
    private ArrayList<PublicacionClass> listaPublic;

    public  MutableLiveData<ArrayList<ParametrosClass>> getMutable() {
        if (mutableList == null) {
            mutableList = new MutableLiveData<ArrayList<ParametrosClass>>();
        }
        mutableList.setValue(lista);
        return mutableList;
    }

    public ViewModelParametros(Resources r, Context c) {
        this.r=r;
        this.c=c;
        //System.out.println("AQUI LLEGA\n");
        String test=cargarPreferenciasParametros();
        //String test="";
        System.out.println("p: "+test);
        //System.out.println(test);
        if(test.length()!=0){lista=descomprimirArray(test);}else{
            lista=new ArrayList<>();
        }
        dbA = new DatabaseAdapter(this);
        idNotification = new MutableLiveData<>();


        //System.out.println(lista);
    }


    public void addParametro(ParametrosClass p){
        //System.out.println(lista);
        //System.out.println(mutableList);
        lista.add(p);
        //System.out.println(lista);
        //System.out.println(mutableList);
        guardarPersistencia();
        mutableList.setValue(lista);
    }
    public void modifyParametro(ParametrosClass p,ParametrosClass b,boolean publicar){

        //System.out.println(lista);
        //System.out.println(mutableList.toString());
        lista.remove(b);
        lista.add(p);
        if(!p.getIdPublicacion().equals("0") && publicar){
            System.out.println("UPDATEEE");
            updatePreferencia(p);
        }else if(p.getIdPublicacion().equals("0") && publicar){
            System.out.println("PUBLICAR");
            subirPreferencia(p);
        }else if(!p.getIdPublicacion().equals("0") && !publicar){
            System.out.println("DELETE");
            ViewModel.getInstance().deletePublicacion(p.getIdPublicacion());
            PublicacionClass pub;
            for (PublicacionClass i : listaPublic) {
                if(i.getIdPublicacion().equals(p.getIdPublicacion())){
                    p.setIdPublicacion("0");
                    listaPublic.remove(i);
                }
            }

        }
        //System.out.println(lista);
        //System.out.println(mutableList.toString());
        guardarPersistencia();
        mutableList.setValue(lista);
    }

    public void changeParametro(ParametrosClass oldP, ParametrosClass newP){
        lista.remove(oldP);
        lista.add(newP);
        guardarPersistencia();
        mutableList.setValue(lista);
    }
    public void subirPreferencia(ParametrosClass par){
        if (currentUser!=null){
            PublicacionClass p;
            HashMap<String, String> coments=new HashMap<>();
            HashMap<String,String> likes = new HashMap<>();
            String parametros=par.toSaveString();
            String idPublicacion="-1";
            String idUsuario=currentUser.getId();
            ViewModel.getInstance().subirPublicacion(coments,likes,parametros,idPublicacion,idUsuario);
            p=new PublicacionClass(coments,likes,par,idPublicacion,idUsuario);
            listaPublic.add(p);
        }
    }
    public void updatePreferencia(ParametrosClass par){
        PublicacionClass p=null;
        for(int i=0; i<listaPublic.size();i++){
            if(listaPublic.get(i).getIdPublicacion().equals(par.getIdPublicacion())){
                p =listaPublic.get(i);
                break;
            }
        }
        if (currentUser!=null && p!= null){
            HashMap<String, String> coments=p.getComentariosList();
            HashMap<String,String> likes = new HashMap<>();
            for (HashMap.Entry<String, Integer> entry : p.getLikeList().entrySet()) {
                likes.put(entry.getKey(), entry.getValue().toString() );
            }
            String parametros=p.getParametros().toSaveString();
            String idPublicacion=p.getIdPublicacion();
            if(idPublicacion.equals("0")){
                //System.out.println("CREAR NUEVA PUBLICACION - ERROR");
            }
            String idUsuario=p.getIdUsuario();
            ViewModel.getInstance().updatePublicacion(coments,likes,parametros,idPublicacion,idUsuario);
        }
    }
    public void updatePublicacion(PublicacionClass p){
        for(int i=0; i<listaPublic.size();i++){
            if(listaPublic.get(i).getIdPublicacion().equals(p.getIdPublicacion())){
                listaPublic.get(i).setLikeList(p.getLikeList());
                listaPublic.get(i).setComentariosList(p.getComentariosList());
                break;
            }
        }
        if (currentUser!=null && p!= null){
            HashMap<String, String> coments=p.getComentariosList();
            HashMap<String,String> likes = new HashMap<>();
            for (HashMap.Entry<String, Integer> entry : p.getLikeList().entrySet()) {
                likes.put(entry.getKey(), entry.getValue().toString() );
            }
            String parametros=p.getParametros().toSaveString();
            String idPublicacion=p.getIdPublicacion();
            if(idPublicacion.equals("0")){
                //System.out.println("CREAR NUEVA PUBLICACION - ERROR");
            }
            String idUsuario=p.getIdUsuario();
            ViewModel.getInstance().updatePublicacion(coments,likes,parametros,idPublicacion,idUsuario);
        }
    }

    public void deleteParametro(ParametrosClass p){
        //System.out.println(lista);
        //System.out.println(mutableList);
        try{
            if(p.getIdPublicacion().length()>4){
                PublicacionClass pub=null;
                for(int i=0; i<listaPublic.size();i++){
                    if(listaPublic.get(i).getIdPublicacion().equals(p.getIdPublicacion())){
                        pub =listaPublic.get(i);
                        break;
                    }
                }
                if (currentUser!=null && pub!=null){
                    listaPublic.remove(pub);
                    ViewModel.getInstance().deletePublicacion(p.getIdPublicacion());

                }
            }

            lista.remove(p);

        }catch (Exception e){
            //System.out.println(e);
        }

        //System.out.println(lista);
        //System.out.println(mutableList);
        guardarPersistencia();
        mutableList.setValue(lista);
    }
    public void deleteALL(){
        guardarPreferenciasParametros("");
        lista=new ArrayList<>();
        lista.add(new ParametrosClass());
        //System.out.println(lista);
        mutableList = new MutableLiveData<ArrayList<ParametrosClass>>();
        mutableList.setValue(lista);
    }

    private void guardarPersistencia() {
        ArrayList<ParametrosClass> pers=new ArrayList<>();
        for(int i=0; i<lista.size();i++){
            if(lista.get(i).getIdPublicacion().equals("0")){
                pers.add(lista.get(i));
            }
        }
        if(pers.size()!=0){
            guardarPreferenciasParametros(comprimirArray(pers));
        }
    }

    public Resources getResources() {
        return r;
    }
    public void setResources(Resources r) {
        this.r= r;
    }

    public static ViewModelParametros getSingletonInstance() {
        return SINGLETON_INSTANCE;
    }
    public static ViewModelParametros getSingletonInstance(Resources r,Context c) {
        if(SINGLETON_INSTANCE == null) {
            SINGLETON_INSTANCE = new ViewModelParametros(r,c);
        }

        return SINGLETON_INSTANCE;
    }

    public ArrayList<ParametrosClass> getLista() {
        return lista;
    }

    private void guardarPreferenciasParametros(String string) {
        SharedPreferences preferencias = c.getSharedPreferences("parametros", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("parametros",string);
        editor.commit();
    }

    private String cargarPreferenciasParametros() {
        SharedPreferences preferencias = c.getSharedPreferences("parametros",MODE_PRIVATE);
        return preferencias.getString("parametros",comprimirArray(fillParametrosList()));
        //return preferencias.getString("parametros",comprimirArray(fillParametrosList()));
    }
    private ArrayList<ParametrosClass> fillParametrosList() {
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        parametrosList.add(new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Playa",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Vela ligera",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Kayak",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("KiteSurf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("WindSurf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Buceo",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Volley Playa", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        return parametrosList;
    }
    private String comprimirArray(ArrayList<ParametrosClass> l){
        String str="";
        for(int i=0; i<l.size();i++){
            str=str+l.get(i).toSaveString()+"¿";
        }
        return str;
    }
    private ArrayList<ParametrosClass> descomprimirArray(String l){
        //System.out.println("HEHHEHEHEHHEHEH");
        //System.out.println(l);
        String[] parametrosStringList = l.split("¿");
        int count = l.length() - l.replace("¿", "").length();
        //System.out.println(count);
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        String[] fixedParam;
        for (String i : parametrosStringList) {
            //System.out.println(i);
            fixedParam=i.split(",");
            for (String x : fixedParam) {
                //System.out.println(x);
            }
            parametrosList.add(new ParametrosClass(fixedParam[0], Float.parseFloat(fixedParam[1]),Float.parseFloat(fixedParam[2]),Float.parseFloat(fixedParam[3]),Float.parseFloat(fixedParam[4]),Float.parseFloat(fixedParam[5]),Float.parseFloat(fixedParam[6]), new DatoGradosClass(Directions.valueOf(fixedParam[7])),Float.parseFloat(fixedParam[8]),Float.parseFloat(fixedParam[9]),Float.parseFloat(fixedParam[10]),Float.parseFloat(fixedParam[11]),new DatoGradosClass(Directions.valueOf(fixedParam[12])), fixedParam[13]));
        }

        return parametrosList;
    }


    public List<PublicacionClass> getPublications(){
        List<PublicacionClass> publicaciones = new ArrayList<>();
        if(currentUser!=null){
            //System.out.println("USUARIO LOGEADOOOO");
            //DatabaseAdapter.getPublications;
            //publicaciones=fillPublicacionList();

            update(publicaciones);
            return listaPublic;
        }
        return publicaciones;
    }
    public void setPublications(List<PublicacionClass> l){
        for (PublicacionClass i : l) {
            lista.add(i.getParametros());
        }
    }
    public void update(List<PublicacionClass> l){

        List<PublicacionClass> l2=new ArrayList<>();
        for (PublicacionClass i : l) {
            for (ParametrosClass k : lista) {
                if(i.getIdPublicacion().equals(k.getIdPublicacion())){
                    i.setParametros(k);
                    l2.add(i);
                }
            }
        }
        l=l2;

    }


    @Override
    public void setStatusLogIn(boolean status) {

    }

    @Override
    public void setUserID(String id) {

    }

    @Override
    public void setUser(UserClass u) {
        currentUser = u;
        if(u==null){
            ArrayList<ParametrosClass> temp=new ArrayList<>();
            for (ParametrosClass k : lista) {
                if(!k.getIdPublicacion().equals("0")){
                    temp.add(k);
                }
            }
            for (ParametrosClass k : temp) {
                lista.remove(k);
            }
        }
    }

    @Override
    public void setToast(String s) {

    }

    public void notifyId(String id) {
        for (PublicacionClass k : listaPublic){
            if (k.getIdPublicacion().equals("-1")){
                k.setIdPublicacion(id);
                k.getParametros().setIdPublicacion(id);
            }
        }
    }

    public void setListaPublicacion(ArrayList<PublicacionClass> publicacionClasses) {
        //System.out.println("PUBLICACIONES RELLENADAS\n");
        this.listaPublic= (ArrayList<PublicacionClass>) publicacionClasses;
        //System.out.println(lista.size());
        boolean existe=false;
        for (PublicacionClass i : publicacionClasses) {
            for (ParametrosClass k : lista) {
                if( i.getIdPublicacion().equals(k.getIdPublicacion())){
                    existe=true;
                }
            }
            if(!existe){
                lista.add(i.getParametros());
                existe=false;
            }
        }
        //System.out.println(lista.size());
    }


    private List<PublicacionClass> fillPublicacionList() {
        List<PublicacionClass> publicacionList= new ArrayList<PublicacionClass>();
        //String nombreActividad, int userID, float presionMax, float presionMin, float temperaturaMax, float temperaturaMin, float vientoMax, float vientoMin, DatoGradosClass directionViento, float alturaOlaMax, float alturaOlaMin, float periodoOlaMax, float periodoOlaMin, DatoGradosClass directionOlas
        ParametrosClass p1= new ParametrosClass("1","SurfLoco",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.NORTE),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.SUD));
        HashMap<String, Integer> likeList1=new HashMap<>();
        likeList1.put("0000",1);
        likeList1.put("0001",1);
        likeList1.put("0002",0);
        HashMap<String, String> comentariosList1=new HashMap<>();
        comentariosList1.put("Carlos Chun","Oye, ta guapo etooo");
        comentariosList1.put("Oscaroca","Esta guay para disfrutar del restaurante que hay al lado.");
        comentariosList1.put("Mi pana miguel","Viva er beti");
        comentariosList1.put("Racsor","Fumas?");
        comentariosList1.put("Mikol","Jo parlo catalÃ¡");
        comentariosList1.put("Sara","Totorooooo");
        comentariosList1.put("Matt","Has visto como entrenar a tu dragon?");
        comentariosList1.put("Septimus","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        publicacionList.add(new PublicacionClass("1",p1,likeList1,comentariosList1));
        ParametrosClass p2= new ParametrosClass("2","Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList2=new HashMap<>();
        likeList2.put("0000",0);
        likeList2.put("0001",0);
        likeList2.put("0002",0);
        HashMap<String, String> comentariosList2=new HashMap<>();
        comentariosList2.put("0001","---");
        comentariosList2.put("0021","---");
        comentariosList2.put("0031","---");
        publicacionList.add(new PublicacionClass("2",p2,likeList2,comentariosList2));
        ParametrosClass p3= new ParametrosClass("3","DokkanBattle", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList3=new HashMap<>();
        likeList3.put("0000",1);
        likeList3.put("0001",1);
        likeList3.put("0002",1);
        HashMap<String, String> comentariosList3=new HashMap<>();
        comentariosList3.put("0001","---");
        comentariosList3.put("0021","---");
        publicacionList.add(new PublicacionClass("3",p3,likeList3,comentariosList3));

        return publicacionList;
    }

    public ParametrosClass getParametroPorId(String s) {
        for (ParametrosClass i : lista) {
            if(i.getIdPublicacion().equals(s)){
                return i;
            }
        }
        return null;
    }

    @Override
    public void setNotificationId(String id) {
        System.out.println("docid: " + id);
        ParametrosClass newParametro = currentParametro;
        newParametro.setIdNotification(id);
        changeParametro(currentParametro, newParametro);
    }

    public void deleteNotification(String idNotification, ParametrosClass old){
        System.out.println("aver:2" + old.toString());
        dbA.deleteNotification(idNotification);
        ParametrosClass newP = old;
        newP.setIdNotification("0");
        changeParametro(old, newP);
    }
    public void createCoordsNotification(String coords, Map<String, Object> data, ParametrosClass old) {
        dbA.createCoordsNotification(coords, data);
        currentParametro = old;
    }

    public MarcadorClass getMarcador() {
        return marcador;
    }

    public void setMarcador(MarcadorClass marcador) {
        this.marcador = marcador;
    }
}


