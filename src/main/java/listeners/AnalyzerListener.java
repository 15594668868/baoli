package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ConfigFile;

public class AnalyzerListener implements IRetryAnalyzer {

    private int retryCount = 0;
    private final int MAX_RETRY_COUNT = Integer.parseInt(ConfigFile.getValue("max.retry.count"));


    /**
     * 设置testng用例失败重试次数
     * @param result
     * @return
     */
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}
