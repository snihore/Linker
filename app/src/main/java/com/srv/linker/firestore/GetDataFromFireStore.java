package com.srv.linker.firestore;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.srv.linker.CustomListViewOne;
import com.srv.linker.CustomListViewTwo;
import com.srv.linker.ProgressAndResult;
import com.srv.linker.algo.Sorting;
import com.srv.linker.data.Link;
import com.srv.linker.data.LinkPOJO;
import com.srv.linker.data.NamesPoJo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDataFromFireStore {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private CollectionReference urlCollection;
    private CollectionReference domainCollection;
    private CollectionReference protocolCollection;

    public GetDataFromFireStore() {
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

    public void getProtocols(final Context context, final ListView listView, final ProgressAndResult progressAndResult)throws Exception{

        progressAndResult.toggleProgressBar02(true);
        this.protocolCollection.document("protocol_names").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                progressAndResult.toggleProgressBar02(false);

                if(documentSnapshot.exists()){
                    NamesPoJo protocolNames = documentSnapshot.toObject(NamesPoJo.class);

                    if(protocolNames != null){

                        List<String> list = protocolNames.getList();

                        listView.setAdapter(new CustomListViewOne(context, list));

                    }else{
                        // Domains Not Found
                        progressAndResult.showToast("Protocols Not Found #1");
                    }

                }else{
                    // Domains Not Found
                    progressAndResult.showToast("Protocols Not Found #2");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressAndResult.toggleProgressBar02(false);
                progressAndResult.showToast(e.getMessage());
//                     e.getMessage();
            }
        });

    }

    public void getDomains(final Context context, final ListView listView, final ProgressAndResult progressAndResult)throws Exception{

        progressAndResult.toggleProgressBar02(true);
        this.domainCollection.document("domain_names").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                progressAndResult.toggleProgressBar02(false);

                if(documentSnapshot.exists()){
                    NamesPoJo domainNames = documentSnapshot.toObject(NamesPoJo.class);

                    if(domainNames != null){

                        List<String> list = domainNames.getList();

                        listView.setAdapter(new CustomListViewOne(context, list));

                    }else{
                        // Domains Not Found
                        progressAndResult.showToast("Domains Not Found #1");
                    }

                }else{
                    // Domains Not Found
                    progressAndResult.showToast("Domains Not Found #2");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressAndResult.toggleProgressBar02(false);
                progressAndResult.showToast(e.getMessage());
//                     e.getMessage();
            }
        });

    }

    public void getProtocolLinks(String protocolName, final Context context, final ListView listView, final ProgressAndResult progressAndResult) throws Exception{

        progressAndResult.toggleProgressBar02(true);

        protocolCollection.document(protocolName).collection("link").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots.size() > 0){

                    List<String> linkIDs = new ArrayList<>();

                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){

                        Map<String, Object> map = documentSnapshot.getData();

                        if(map != null){
                            String linkID = (String)map.get("linkID");

                            linkIDs.add(linkID);

                        }

                    }

                    //Get Link From FireStore ...
                    if(linkIDs.size()>0){

                        getLinks(linkIDs, context, listView, progressAndResult);

                    }else{
                        progressAndResult.showToast("Links not found #1");
                        progressAndResult.toggleProgressBar02(false);
                    }

                }else{
                    progressAndResult.showToast("Links not found #2");
                    progressAndResult.toggleProgressBar02(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressAndResult.toggleProgressBar02(false);
                progressAndResult.showToast(e.getMessage());
            }
        });
    }

    public void getDomainLinks(String domainName, final Context context, final ListView listView, final ProgressAndResult progressAndResult) throws Exception{

        progressAndResult.toggleProgressBar02(true);

        domainCollection.document(domainName).collection("link").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots.size() > 0){

                    List<String> linkIDs = new ArrayList<>();

                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){

                        Map<String, Object> map = documentSnapshot.getData();

                        if(map != null){
                            String linkID = (String)map.get("linkID");

                            linkIDs.add(linkID);

                        }

                    }

                    //Get Link From FireStore ...
                    if(linkIDs.size()>0){

                        getLinks(linkIDs, context, listView, progressAndResult);

                    }else{
                        progressAndResult.showToast("Links not found #1");
                        progressAndResult.toggleProgressBar02(false);
                    }

                }else{
                    progressAndResult.showToast("Links not found #2");
                    progressAndResult.toggleProgressBar02(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressAndResult.toggleProgressBar02(false);
                progressAndResult.showToast(e.getMessage());
            }
        });
    }

    private void getLinks(List<String> linkIDs, final Context context, final ListView listView, final ProgressAndResult progressAndResult) {

        final List<Link> list = new ArrayList<>();

        for(String linkID: linkIDs){

            urlCollection.document(linkID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    progressAndResult.toggleProgressBar02(false);

                    if(documentSnapshot.exists()){

                        LinkPOJO linkPOJO = (LinkPOJO) documentSnapshot.toObject(LinkPOJO.class);

                        if(linkPOJO != null){
                            Link link = linkPOJO.getLink();

                            if(link != null){

                                list.add(link);

                                if(list.size()>0){

                                    //View Links ...
                                    try{
                                        Sorting sorting = new Sorting();
                                        List<Link> list2 = sorting.SortLinkList(list);
                                        listView.setAdapter(new CustomListViewTwo(context, list2));
                                    }catch (Exception e){
                                        progressAndResult.showToast(e.getMessage());
                                        listView.setAdapter(new CustomListViewTwo(context, list));
                                    }

                                }

                            }
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressAndResult.toggleProgressBar02(false);
                    progressAndResult.showToast(e.getMessage());
                }
            });
        }
    }

    public void getAllLinks(final Context context, final ListView listView, final ProgressAndResult progressAndResult) throws Exception{


        final List<Link> list = new ArrayList<>();

        progressAndResult.toggleProgressBar02(true);

        urlCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                progressAndResult.toggleProgressBar02(false);

                if(!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots.size()>0){

                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){

                        LinkPOJO linkPOJO = documentSnapshot.toObject(LinkPOJO.class);

                        if(linkPOJO != null){

                            Link link = linkPOJO.getLink();

                            if(link != null){

                                list.add(link);

                                if(list.size()>0){

                                    //View Links ...

                                    try{
                                        Sorting sorting = new Sorting();
                                        List<Link> list2 = sorting.SortLinkList(list);
                                        listView.setAdapter(new CustomListViewTwo(context, list2));
                                    }catch (Exception e){
                                        progressAndResult.showToast(e.getMessage());
                                        listView.setAdapter(new CustomListViewTwo(context, list));
                                    }


                                }

                            }
                        }

                    }

                }else{

                    progressAndResult.showToast("Links Not Found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressAndResult.toggleProgressBar02(false);
                progressAndResult.showToast(e.getMessage());

            }
        });
    }
}
