# MMDS-Amazon-Reviews

Being able to compare products online revolutionized the experience of shoppers around the world. However, due to ever-increasing scale of online marketplaces such as Amazon.com, Alibaba.com, and eBay, it has become impossible for consumers and businesses alike to evaluate features for every product in the desired search space.

In this project we find product feature summaries based only on natural text reviews. We use review data from Amazon.com from the excellent [Amazon product reviews](http://jmcauley.ucsd.edu/data/amazon/) dataset provided by Julian McAuley. The work was done for the seminar [Mining Massive Datasets](https://hpi.de/naumann/teaching/current-courses/ss-16/mmds.html) at Hasso Plattner Institute in Potsdam, Germany.

The pipeline—as described below—is implemented as [Apache Spark](http://spark.apache.org/) jobs in Java and Scala. It can be run locally and on Spark clusters.

## Algorithm

We employ a three-step pipeline: Feature Extraction, Feature Clustering, and Modifier Weighting which are explained below.

### Definitions

A *Feature* in our sense is a word (-group) that stands for a product feature, such as `display`, `battery`, or `color temperature`.
A *Modifier* is a word (-group) that describes a feature, such as `better`, `large`, or `the coolest`.

### Feature Extraction

#### Filtering

We use filters to narrow down reviews to products of specific categories, brands, and price ranges. There are four filters currently implemented:

| Filter Class   | Input                                          | Output                                     |
|----------------|------------------------------------------------|--------------------------------------------|
| SampleFilter   | List of Products, Sample Fraction (0..1), Seed | Sampled list of products                   |
| BrandFilter    | List of Products, Brand Name                   | Only Products of the given brand           |
| CategoryFilter | List of Products, Category Name                | Only products of the given (sub-) category |
| PriceFilter    | List of Products, Minimum Price, Maximum Price | Only products of the given price range     |

In the consecutive steps, only reviews with matching product ids will be evaluated.

#### Building NGrams from Templates


### Feature Clustering



### Modifier Weighting



## Performance

### Scale-out

The following experiments were done using a cluster with these specifications:

* Master: Dell PowerEdge R310 (4x2.66 GHz, 8 GB RAM)
* 10 Slave Nodes: Dell OptiPlex 780 (2x2.6 GHz, 8 GB RAM)
* Shared HDFS

#### Runtime
![Runtime Chart](/doc/runtime.png?raw=true "Runtime / Number of Cores")
#### Relative Speedup
![Relative Speedup Chart](/doc/relative_speedup.png?raw=true "Relative Speedup / Number of Cores")
#### Percentage Improvement
![Percentage Improvement Chart](/doc/percentage_improvement.png?raw=true "Percentage Improvement / Number of Cores")

## Datasets

We use Amazon.com product reviews as provided in the [Amazon product reviews](http://jmcauley.ucsd.edu/data/amazon/). The program will only accept reviews and metadata in JSON format from the local file system or the Hadoop filesystem (HDFS).

## Running the Code

### Locally

1. `mvn clean install`
2. `java -jar target/mmds-[version].jar [parameters, see below]`

### On a Spark Cluster

1. `mvn clean install`
2. `mvn package`
3. `spark-submit --deploy-mode client --jars $(echo target/lib/*.jar | tr ' ' ',') target/mmds-[version].jar [parameters, see below]`

Use `spark-submit` parameters like `--executor-memory 4G --driver-memory 8G --total-executor-cores 20` to scale.

### Parameters

The program accepts the following parameters:

`.../mmds-[version].jar [ReviewFilePath] [NumberOfPartitions] [ClusteringAlgorithm] [MetadataFilePath]`

| Parameter           | Expected Value                                                                                     | Example                                                      |
|---------------------|----------------------------------------------------------------------------------------------------|--------------------------------------------------------------|
| ReviewFilePath      | A valid file path on the local or Hadoop file system. The file must contain review JSON objects.   | `resources/samples/musical_instruments_top100.json`          |
| NumberOfPartitions  | Integer. Number of partitions used to parallelize Spark jobs.                                      | `4`                                                          |
| ClusteringAlgorithm | String. One of [TreeAggregate, DIMSUM, ExactMatch]. Default is `ExactMatch`.                       | `DIMSUM`                                                     |
| MetadataFile        | A valid file path on the local or Hadoop file system. The file must contain metadata JSON objects. | `resources/samples/musical_instruments_metadata_top100.json` |



----------------------------------------------------
Maximilian Grundke, Axel Kroschk, Jaspar Mang, 2016.