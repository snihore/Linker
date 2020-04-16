package com.srv.linker.algo;

import com.srv.linker.data.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sorting {

    public List<Link> SortLinkList(List<Link> links) throws Exception{

        Link[] links2 = new Link[links.size()];

        //Initialise Array[Link] from List[Link]
        for(int i=0; i<links2.length; i++){

            links2[i] = links.get(i);
        }

        // INSERTION SORT ...
        int i, j;
        Link key;
        for (i = 1; i < links2.length; i++)
        {
            key = links2[i];
            j = i - 1;

        /* Move elements of arr[0..i-1], that are
        greater than key, to one position ahead
        of their current position */
            while (j >= 0 && links2[j].getTimestamp() > key.getTimestamp())
            {
                links2[j + 1] = links2[j];
                j = j - 1;
            }
            links2[j + 1] = key;
        }

        //Create sorted List<Link> from Array[Link]
        List<Link> list2 = new ArrayList<>();

        for(i=links2.length-1; i>=0; i--){

            list2.add(links2[i]);

        }

        return list2;
    }
}
