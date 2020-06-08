package com.srv.linker.algo;


public class GetDomainAndProtocol{

    private String url;

    public GetDomainAndProtocol(String url){
        this.url = url;
    }

    public String getProtocol() throws Exception{

        int limit = 10;
        String s1 = ":", s2 = "//";
        boolean isProtocolHere = false;
        String protocol = "";

        for(int i=0; i<limit; i++){
            String s = this.url.substring(i, i+1);
            if(s.equals(s1)){
                s = this.url.substring(i+1, i+3);
                if(s.equals(s2)){
                    isProtocolHere = true;
                    return protocol;
                }
                break;
            }else{
                protocol += s;
            }
        }

        return null;

    }

    public String getDomain() throws Exception{

        //https://www.youtube.com/watch?v=x_NoA_Fp2Rc

        String[] arr01 = this.url.split("://");

        String urlWithOutProtocol = "";

        if(arr01.length == 2){
            urlWithOutProtocol = arr01[1];
        }else{
            urlWithOutProtocol = arr01[0];
        }

        String[] arr02 = urlWithOutProtocol.split("/");

        if(arr02.length > 0){

            String domain = arr02[0];

            if(domain.substring(0, 4).equals("www.")){
                return domain.substring(4);
            }

            return arr02[0];
        }

        return null;
    }
}
