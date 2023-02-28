package com.example;

import com.azure.core.util.paging.ContinuablePage;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.TaggedBlobItem;
import com.azure.storage.blob.options.FindBlobsOptions;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AzureBlobStoreAsyncAccess {

    @ConfigProperty(name = "azure.account.name")
    String accountName;

    @ConfigProperty(name = "azure.account.key")
    String accountKey;

    @ConfigProperty(name = "tenant.name")
    String tenantName;

    BlobServiceAsyncClient service;

    @PostConstruct
    void connectToAzureBlobStore() {
        String connectionString = """
                DefaultEndpointsProtocol=https;
                AccountName=%s;AccountKey=%s;
                EndpointSuffix=core.windows.net
                """.formatted(accountName, accountKey);
        service = new BlobServiceClientBuilder().connectionString(connectionString).buildAsyncClient();
    }

    public Flux<TaggedBlobItem> searchBlobs(int pageSize) {
        var query = "\"tenant\" = '%s'\n".formatted(tenantName);
        var searchOptions = new FindBlobsOptions(query).setMaxResultsPerPage(pageSize);
        return service
                .findBlobsByTags(searchOptions)
                .byPage(searchOptions.getMaxResultsPerPage())
                .take(1, true)
                .concatMapIterable(ContinuablePage::getElements);
    }

    public Flux<TaggedBlobItem> searchBlobsBug(int pageSize) {
        var query = "\"tenant\" = '%s'\n".formatted(tenantName);
        var searchOptions = new FindBlobsOptions(query).setMaxResultsPerPage(pageSize);
        return service
                .findBlobsByTags(searchOptions)
                .byPage() //the omission of a page size parameter here is the only difference
                .take(1, true)
                .concatMapIterable(ContinuablePage::getElements);
    }

}

