package com.srv.linker.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.srv.linker.ProgressAndResult;
import com.srv.linker.algo.GetDomainAndProtocol;
import com.srv.linker.data.DomainPOJO;
import com.srv.linker.data.ProtocolPOJO;

public class DeleteDataFromFireStore {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private CollectionReference urlCollection;
    private CollectionReference domainCollection;
    private CollectionReference protocolCollection;

    private String uniqueID, URL;

    public DeleteDataFromFireStore(String uniqueID, String URL) {
        this.uniqueID = uniqueID;
        this.URL = URL;
        initFirestore();
    }


    private void initFirestore() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.documentReference = this.firebaseFirestore.collection("Linker")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        this.urlCollection = this.documentReference.collection("URLs");
        this.domainCollection = this.documentReference.collection("Domains");
        this.protocolCollection = this.documentReference.collection("Protocols");
    }

    public void deleteURL(final ProgressAndResult progressAndResult) throws Exception{

        if(!this.uniqueID.matches("") && !this.URL.matches("")){

            GetDomainAndProtocol getDomainAndProtocol = new GetDomainAndProtocol(URL);

            final String domainName = getDomainAndProtocol.getDomain();
            final String protocolName = getDomainAndProtocol.getProtocol();


            //Finally DELETED from URLs ...

            this.urlCollection.document(uniqueID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    //Delete from domain and protocol first as well

                    deleteDomain(domainName);

                    deleteProtocol(protocolName);

                    progressAndResult.showToast("deleted");

                    progressAndResult.getDataFromAllTab();
                    progressAndResult.getDataFromLinksViewActivity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressAndResult.showToast(e.getMessage());
                }
            });

        }else {
            progressAndResult.showToast("some error, try again");
        }

    }

    private void deleteDomain(final String domainName) {
        this.domainCollection.document(domainName).collection("link").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(DocumentSnapshot snapshot: task.getResult().getDocuments()){


                        if(snapshot != null && snapshot.getId() != null ){

                            DomainPOJO domainPOJO = (DomainPOJO)snapshot.toObject(DomainPOJO.class);

                            if(domainPOJO != null && uniqueID.equals(domainPOJO.getLinkID())){
                                Log.i("DOMAIN DEL", "successfull");
                                //DELETE
                                domainCollection.document(domainName).collection("link").document(snapshot.getId()).delete();
                            }else{
                                Log.i("DOMAIN DEL", "not successfull inner inner");
                                Log.i("ID", snapshot.getId());
                                Log.i("UNI ID", uniqueID);
                            }

                        }else{
                            Log.i("DOMAIN DEL", "not successfull inner");
                        }
                    }

                }else{
                    Log.i("DOMAIN DEL", "not successfull");
                }
            }
        });
    }

    private void deleteProtocol(final String protocolName) {

        this.protocolCollection.document(protocolName).collection("link").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(DocumentSnapshot snapshot: task.getResult().getDocuments()){

                        if(snapshot != null && snapshot.getId() != null ){

                            ProtocolPOJO protocolPOJO = (ProtocolPOJO) snapshot.toObject(ProtocolPOJO.class);

                            if(protocolPOJO != null && uniqueID.equals(protocolPOJO.getLinkID())){
                                Log.i("PROTOCOL DEL", "successfull");
                                //DELETE
                                protocolCollection.document(protocolName).collection("link").document(snapshot.getId()).delete();
                            }else{
                                Log.i("PROTOL DEL", "not successfull inner inner");
                                Log.i("ID", snapshot.getId());
                                Log.i("UNI ID", uniqueID);
                            }

                        }else{
                            Log.i("PROTOCOL DEL", "not successfull inner");
                        }
                    }

                }else{
                    Log.i("PROTOCOL DEL", "not successfull");
                }
            }
        });
    }
}
