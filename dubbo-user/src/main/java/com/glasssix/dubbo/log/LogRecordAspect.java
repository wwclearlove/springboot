package com.glasssix.dubbo.log;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
//@Aspect
@Component
@AllArgsConstructor
public class LogRecordAspect {


//	ApplicationContext applicationContext;
//	CacheService cacheService;
//	LogService logService;
//	private static final ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();
//	private final ThreadLocal<String> threadLocalData = new ThreadLocal<>();
//
//	@Pointcut("@annotation(com.glasssix.box.annotation.LogRecord)")
//	private void logValid() {
//
//	}
//
//	@Before(value = "logValid()")
//	public void doBefore(JoinPoint joinPoint) {
//		// 获取注解信息
//		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//		LogRecord logRecord = method.getAnnotation(LogRecord.class);
//		String logClassName = logRecord.className();
//		String logMethodName = logRecord.methodName();
//		// 反射获取日志处理类中的方法并执行
//		try {
//			Class<?> clazz = Class.forName(logClassName);
//			Object bean = applicationContext.getBean(clazz);
//			Method targetMethod = clazz.getMethod(logMethodName, String.class);
//			String parameter = getParameter(logRecord, joinPoint);
//			String data = targetMethod.invoke(bean, parameter).toString();
//			threadLocalData.set(data);
//		} catch (Exception ex) {
//			log.error("execute log failed: {}", ex.getMessage());
//		}
//	}
//
//	@AfterReturning(pointcut = "logValid()")
//	public void doAfterReturning(JoinPoint joinPoint) {
//		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//		LogRecord logRecord = method.getAnnotation(LogRecord.class);
//		ModuleEnum m= logRecord.module();
//		try {
//			String data = threadLocalData.get();
//			String token = HeaderUtils.getToken();
//			LoginTokenVO tokenVO = cacheService.getTokenOrException(token);
//			Log operateLog = Log.builder().module(m).operation(data).ip(HeaderUtils.getIp()).account(tokenVO.getAccount()).userId(tokenVO.getUserId()).build();
//			logService.save(operateLog);
//		} catch (Exception exception) {
//			log.error("添加日志异常{}", exception.getMessage());
//		} finally {
//			threadLocalData.remove();
//		}
//
//	}
//
//	private String getParameter(LogRecord handler, JoinPoint joinPoint) {
//		if (ObjectUtils.isEmpty(handler.parameter())) {
//			return null;
//		}
//		EvaluationContext evaluationContext = evaluator.createEvaluationContext(joinPoint.getTarget(), joinPoint.getTarget().getClass(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
//		AnnotatedElementKey methodKey = new AnnotatedElementKey(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getTarget().getClass());
//		return evaluator.condition(handler.parameter(), methodKey, evaluationContext, String.class);
//	}
}

