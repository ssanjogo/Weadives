package com.example.weadives.PantallaPerfilAmigo;

import com.example.weadives.ParametrosClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PublicacionClass {


    private ParametrosClass parametros;
    private HashMap<String, Integer> likeList;
    private HashMap<String, String> comentariosList;
    private String idPublicacion;
    private String  idUsuario;


    public PublicacionClass(HashMap<String, String> map_comentarios, HashMap<String, String> map_likes, ParametrosClass p, String idPubli, String idUsuario) {
        this.parametros = p;
        HashMap<String,Integer> newMap = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map_likes.entrySet()) {
                newMap.put(entry.getKey(), Integer.valueOf(entry.getValue()) );
        }
        this.likeList = newMap;
        System.out.println(map_likes.getClass());
        System.out.println(map_likes);
        this.comentariosList = map_comentarios;
        this.idPublicacion = idPubli;
        System.out.println("TESTEO-\n");
        System.out.println(idPubli);
        p.setIdPublicacion(idPubli);
        this.idUsuario=idUsuario;
    }
    public PublicacionClass(ParametrosClass p, String idPubli, String idUsuario,HashMap<String, String> map_comentarios, HashMap<String, Integer> map_likes) {
        this.parametros = p;
        this.likeList = map_likes;
        System.out.println(map_likes.getClass());
        System.out.println(map_likes);
        this.comentariosList = map_comentarios;
        this.idPublicacion = idPubli;
        this.idUsuario=idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ParametrosClass getParametros() {
        return parametros;
    }

    public void setParametros(ParametrosClass parametros) {
        this.parametros = parametros;
    }

    public PublicacionClass(String idPublicacion, ParametrosClass parametros, HashMap<String, Integer> likeList, HashMap<String, String> comentariosList) {
        this.parametros = parametros;
        this.likeList = likeList;
        this.comentariosList = comentariosList;
        this.idPublicacion = idPublicacion;
    }
    public boolean like(String id, int votacion){
        if(likeList.containsKey(id)) {
            if (likeList.get(id) == -1) {
                likeList.put(id,votacion);
                return true;
            } else {
                return false;
            }
        }
        likeList.put(id,votacion);
        return true;
    }

    public boolean containlike(String id, int votacion){
        if(likeList.containsKey(id)){

            if(likeList.get(id) == (votacion)){
                return true;
            }return false;
        }
        return false;
    }

    public boolean removeLike(String id){
        if(!likeList.containsKey(id)){
            return false;}
        likeList.remove(id);
        return true;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public void addComment(String id, String comment){
        comentariosList.put(id,comment);

    }
    private void removeComment(String id){
        comentariosList.remove(id);
    }

    public HashMap<String, Integer> getLikeList() {
        return likeList;
    }

    public List<String> getCommentInList(){
        ArrayList<String> list=new ArrayList<>();
        String value;
        String key;
        for(Map.Entry<String, String> entry : comentariosList.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            list.add(key + "  \n"+value);
        }
        return list;
    }

    public int getNumLikes(){
        int sum=0;
        int value;
        String key;
        //System.out.println("Num of likes : \n");
        //System.out.println(likeList);
        for(HashMap.Entry<String, Integer> entry : likeList.entrySet()) {
            //System.out.println(entry.getKey());
            //System.out.println(entry.getValue());
            Set s=likeList.entrySet();
            key = entry.getKey();
            value = entry.getValue();
            if (value==1){
                sum++;
            }
        }
        return sum;
    }
    public int getNumDislikes(){
        int sum=0;
        int value;
        String key;
        for(Map.Entry<String, Integer> entry : likeList.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value==0){
                sum++;
            }
        }
        return sum;
    }
    public int getNumComments(){
        return comentariosList.size();
    }

    public void setLikeList(HashMap<String, Integer> likeList) {
        this.likeList = likeList;
    }

    public HashMap<String, String> getComentariosList() {
        return comentariosList;
    }

    public void setComentariosList(HashMap<String, String> comentariosList) {
        this.comentariosList = comentariosList;
    }

    @Override
    public String toString() {
        return "PublicacionClass{" +
                "parametros=[" + parametros.toString() +
                "], likeList=" + likeList +
                ", comentariosList=" + comentariosList +
                '}';
    }
}
