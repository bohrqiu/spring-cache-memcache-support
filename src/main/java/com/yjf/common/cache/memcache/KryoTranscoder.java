package com.yjf.common.cache.memcache;

import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yjf.common.lang.util.KryoUtil;

/**
 * kryo编解码器
 *                       
 * @Filename KryoTranscoder.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author bohr.qiu
 *
 * @Email qzhanbo@yiji.com
 *       
 * @History
 *<li>Author: bohr.qiu</li>
 *<li>Date: 2013-4-1</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class KryoTranscoder extends SerializingTranscoder {
	
	@Override
	protected byte[] serialize(Object o) {
		Kryo kryo = KryoUtil.get();
		Output output = new Output(KryoUtil.INITIAL_BUFFER_SIZE, KryoUtil.MAX_BUFFER_SIZE);
		kryo.writeClassAndObject(output, o);
		byte[] buffer = output.getBuffer();
		return buffer;
	}
	
	@Override
	protected Object deserialize(byte[] in) {
		Kryo kryo = KryoUtil.get();
		Input input = new Input();
		input.setBuffer(in);
		return kryo.readClassAndObject(input);
	}
	
}
