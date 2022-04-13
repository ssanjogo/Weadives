package com.example.weadives.PantallaPerfilAmigo;

import com.example.weadives.ParametrosClass;

import java.util.HashMap;
import java.util.Map;

public class PublicacionClass {
    public ParametrosClass getParametros() {
        return parametros;
    }

    public void setParametros(ParametrosClass parametros) {
        this.parametros = parametros;
    }

    private ParametrosClass parametros;
    private HashMap<String, Integer> likeList;
    private HashMap<String, String> comentariosList;

    public PublicacionClass(ParametrosClass parametros, HashMap<String, Integer> likeList, HashMap<String, String> comentariosList) {
        this.parametros = parametros;
        this.likeList = likeList;
        this.comentariosList = comentariosList;
    }
    private void like(String id, int votacion){
        likeList.put(id,votacion);
    }
    private void removeLike(String id){
        likeList.remove(id);
    }

    private void addComment(String id, String comment){
        comentariosList.put(id,comment);

    }
    private void removeComment(String id){
        comentariosList.remove(id);
    }

    public HashMap<String, Integer> getLikeList() {
        return likeList;
    }
    public int getNumLikes(){
        int sum=0;
        int value;
        String key;
        for(Map.Entry<String, Integer> entry : likeList.entrySet()) {
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
