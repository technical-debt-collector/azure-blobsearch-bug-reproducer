package com.example;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(SearchTest.class, args);
    }

    public static class SearchTest implements QuarkusApplication {

        @Inject
        AzureBlobStoreAsyncAccess azureBlobStoreAsyncAccess;

        public int run(String... args) {
            System.out.println("Calling searchBlobs:");
            var searchBlobs = azureBlobStoreAsyncAccess.searchBlobs(25).collectList().block();
            System.out.println("searchBlobs.size() = " + searchBlobs.size());

            System.out.println("Calling searchBlobsBug next:");
            var searchBlobsBug = azureBlobStoreAsyncAccess.searchBlobsBug(25).collectList().block();
            System.out.println("searchBlobsBug.size() = " + searchBlobsBug.size());
            return 0;
        }
    }
}