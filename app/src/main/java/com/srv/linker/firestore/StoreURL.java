package com.srv.linker.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srv.linker.ProgressAndResult;
import com.srv.linker.algo.GetDomainAndProtocol;
import com.srv.linker.data.DomainPOJO;
import com.srv.linker.data.Link;
import com.srv.linker.data.LinkPOJO;
import com.srv.linker.data.NamesPoJo;
import com.srv.linker.data.ProtocolPOJO;

public class StoreURL {

    private ProgressAndResult progressAndResult;

    private String urlName;
    private String urlTag;

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private CollectionReference urlCollection;
    private CollectionReference domainCollection;
    private CollectionReference protocolCollection;

    public StoreURL(ProgressAndResult progressAndResult, String urlName) {
        this.urlName = urlName;
        this.urlTag = "";
        this.progressAndResult = progressAndResult;
        initFirestore();

    }

    public StoreURL(ProgressAndResult progressAndResult, String urlName, String urlTag) {
        this.urlName = urlName;
        this.urlTag = urlTag;
        this.progressAndResult = progressAndResult;
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

    public void protocolInit(final String protocolName, final Link link){
        if(protocolName != null){
            final ProtocolPOJO protocolPOJO = new ProtocolPOJO(link.getUniqueID());

            protocolCollection.document("protocol_names").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    NamesPoJo protocolNamesPOJO = null;

                    if(documentSnapshot.exists()){
                        protocolNamesPOJO =  (NamesPoJo) documentSnapshot.toObject(NamesPoJo.class);
                        protocolNamesPOJO.setName(protocolName);
                    }else{
                        protocolNamesPOJO = new NamesPoJo(protocolName);
                    }

                    //Save ProtocolNamesPOJO
                    protocolCollection.document("protocol_names").set(protocolNamesPOJO).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            //Save Actual Link ID to domain ...

                            protocolCollection.document(protocolName).collection("link").add(protocolPOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressAndResult.showToast("Added to protocol");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressAndResult.showToast("Not added to protocol #1");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressAndResult.showToast("Not added to protocol #2");
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressAndResult.showToast("Not added to protocol #3");
                }
            });

        }else{
            progressAndResult.showToast("Not added to protocol #4");
        }
    }

    public void domainInit(final String domainName, final Link link){{
        if(domainName != null){
            final DomainPOJO domainPOJO = new DomainPOJO(link.getUniqueID());

            domainCollection.document("domain_names").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    NamesPoJo domainNamesPOJO = null;

                    if(documentSnapshot.exists()){

                        domainNamesPOJO =  (NamesPoJo) documentSnapshot.toObject(NamesPoJo.class);
                        domainNamesPOJO.setName(domainName);
                    }else{
                        domainNamesPOJO = new NamesPoJo(domainName);
                    }

                    //Save DomainNamesPOJO
                    domainCollection.document("domain_names").set(domainNamesPOJO).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            //Save Actual Link ID to domain ...

                            domainCollection.document(domainName).collection("link").add(domainPOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressAndResult.showToast("Added to domain");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressAndResult.showToast("Not added to domain #1");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressAndResult.showToast("Not added to domain #2");
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressAndResult.showToast("Not added to domain #3");
                }
            });

        }else{
            progressAndResult.showToast("Not added to domain #4");
        }
    }

    }

    public void init() throws Exception{

        final Link link = new Link(urlName, urlTag);

        GetDomainAndProtocol getDomainAndProtocol = new GetDomainAndProtocol(link.getUrlName());

        final String domainName = getDomainAndProtocol.getDomain();
        final String protocolName = getDomainAndProtocol.getProtocol();

        //Store ...

        progressAndResult.toggleProgressBar01(true);
        // 1. Store URL
        LinkPOJO linkPOJO = new LinkPOJO(link);
        urlCollection.document(link.getUniqueID()).set(linkPOJO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressAndResult.showToast("Link saved");

                domainInit(domainName, link);

                protocolInit(protocolName, link);

                progressAndResult.toggleProgressBar01(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressAndResult.showToast("Link not saved");
                progressAndResult.toggleProgressBar01(false);
            }
        });


    }
}
