/**
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package com.yjf.common.lang.result;

import java.io.Serializable;

/**
 *                       
 * @Filename Result.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author peigen
 *
 * @Email peigen@yiji.com
 *       
 * @History
 *<li>Author: peigen</li>
 *<li>Date: 2011-11-11</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public interface Result extends Serializable {
	
	public boolean isSuccess();
	
	public boolean isExecuted();
	
	public String getMessage();
	
}
