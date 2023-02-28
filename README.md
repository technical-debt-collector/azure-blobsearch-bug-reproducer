# azure-bug-reproducer

Reproducer for the issue where the page size search options are not being respected.
https://github.com/Azure/azure-sdk-for-java/issues/33463

## Running the application

The AzureBlobStoreAsyncAccess is the java bean containing all the Azure logic.
Before running, make sure to fill out the application.properties with values for your environment

The class Main will be run when starting the application as described below:

```shell script
./mvnw compile quarkus:dev
```

This application calls the two methods searchBlobs and searchBlobsBug and print
out their result sizes.

The searchBlobs() only seems to work by explicitly providing a page size to the 
byPage()-method which pages the results in the first place, even though a searchOptions 
with the max page size was provided earlier in the code.

The following searchBlobsBug() call demonstrates this by running the same chain of
logic but without this particular method argument.