package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {

    /**
     * 重试监听器，如果测试方法中未添加重试分析器，则默认添加MyRetryAnalyzer作为重试分析器
     * @param annotation
     * @param testClass
     * @param testConstructor
     * @param testMethod
     */
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retryAnalyzer = annotation.getRetryAnalyzer();
        if (null == retryAnalyzer) {
            annotation.setRetryAnalyzer(AnalyzerListener.class);
        }
    }
}
