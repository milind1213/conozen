package com.convozen.TestBase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class SlackIntegration {
    private String channelName;
    private final String botUserOAuthAccessToken;

    public SlackIntegration(String channelName, String botUserOAuthAccessToken) {
        this.channelName = channelName;
        this.botUserOAuthAccessToken = botUserOAuthAccessToken;
    }

    public void sendTestExecutionReportToSlack(String directoryPath, String reportHeader) {
        File latestReport = getLatestFileFromDir(directoryPath, "html");
        System.out.println(" Latest Report Path :" + latestReport);
        if (latestReport != null && latestReport.exists()) {
            System.out.println("Sending Slack notification for project");
            System.out.println("Test Report Path: " + latestReport.getAbsolutePath());

            sendFile(latestReport.getAbsolutePath(), reportHeader + " - HTML Report", "text/html");
        } else {
            System.err.println("Report file does not exist: " + (latestReport != null ? latestReport.getAbsolutePath() : "null"));
        }
    }

    private File getLatestFileFromDir(String dirPath, String extension) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Directory does not exist: " + dirPath);
            return null;
        }
        Optional<File> latestFile = Arrays.stream(dir.listFiles((d, name) -> name.endsWith("." + extension)))
                .max(Comparator.comparingLong(File::lastModified));

        return latestFile.orElse(null);
    }

    public void sendFile(String filePath, String message, String contentType) {
        try {
            String uploadFileMethodURL = "https://slack.com/api/files.upload";
            HttpClient httpClient = HttpClientBuilder.create().disableContentCompression().build();
            HttpPost httpPost = new HttpPost(uploadFileMethodURL);
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.addBinaryBody("file", new File(filePath), ContentType.create(contentType), new File(filePath).getName());
            reqEntity.addTextBody("channels", this.channelName);
            reqEntity.addTextBody("token", this.botUserOAuthAccessToken);
            reqEntity.addTextBody("initial_comment", message);
            httpPost.setEntity(reqEntity.build());
            HttpResponse execute = httpClient.execute(httpPost);

            System.out.println("Execution response:");
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
