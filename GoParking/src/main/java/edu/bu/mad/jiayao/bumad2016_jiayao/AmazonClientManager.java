package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Get clients to the various AWS services
 */
public class AmazonClientManager {

    private AmazonDynamoDBClient ddb = null;
    private Context context;

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public void validateCredentials() {

        if (ddb == null) {
            initClients();
        }
    }

    private void initClients() {
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-west-2:3c2a4308-610a-40a1-86ac-3bd38434fd23", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        ddb = new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_WEST_2));
    }
}
