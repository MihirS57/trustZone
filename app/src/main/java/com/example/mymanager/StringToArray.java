package com.example.mymanager;

public class StringToArray {

    public int[] toArray(String inp){
        int arr[] = new int[10];
        int i;
        for(i=0;i<10;i++){
            arr[i] = Character.getNumericValue( inp.charAt( i ));
        }
        return arr;
    }
    public String convertToString(int arr[]){
        String buf="";
        for(int i = 0;i<10;i++){
            buf=buf+arr[i];
        }
        return buf;
    }

    public String convertAndAdd(String inp,int ind,int mode){
        String buf="";
        int arr[] = new int[10];
        arr = toArray( inp );
        if(mode != 0) { //add operation
            arr[ind] = ind + 1;
        }
        else{           // delete operation
            arr[ind] = 0;
        }
        buf = convertToString( arr );
        return buf;
    }
}
