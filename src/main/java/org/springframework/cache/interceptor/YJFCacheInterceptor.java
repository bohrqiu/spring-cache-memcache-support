package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@SuppressWarnings("serial")
public class YJFCacheInterceptor extends YJFCacheAspectSupport implements MethodInterceptor,
																Serializable {
	/**
	 * 是否启用webservice 结果缓存优化支持，如果启用，则不缓存com.yjf.common.lang.result.Result.success==false的结果对象
	 */
	private boolean	webServiceEnable;
	
	private static class ThrowableWrapper extends RuntimeException {
		private final Throwable	original;
		
		ThrowableWrapper(Throwable original) {
			this.original = original;
		}
	}
	
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		
		Invoker aopAllianceInvoker = new Invoker() {
			public Object invoke() {
				try {
					return invocation.proceed();
				} catch (Throwable ex) {
					throw new ThrowableWrapper(ex);
				}
			}
		};
		
		try {
			return execute(aopAllianceInvoker, invocation.getThis(), method,
				invocation.getArguments());
		} catch (ThrowableWrapper th) {
			throw th.original;
		}
	}
	
	@Override
	public boolean getWebServiceEnable() {
		return webServiceEnable;
	}
	
	public void setWebServiceEnable(boolean webServiceEnable) {
		this.webServiceEnable = webServiceEnable;
	}
}
