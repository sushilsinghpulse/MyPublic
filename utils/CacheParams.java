
package com.perf.utils;

import java.io.*;


/**
 * Holds all information about one cache. 
 * @author chantie
 *
 */
public class CacheParams implements Serializable {
	String name;
	String className;
	int maxElementsInMemory;
	boolean overFlowToDisk;
	int status;
	String idleTime;
	String timeToLive;
	boolean eternal;
	int memoryHits;
	int diskHits;
	int expiredMisses;
	int notFoundMisses;
	int objectSize;
	long memorySizeCount;
	int diskSizeCount;
	int memorySizeBytes;
	int diskSizeBytes;
	
	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		
		strBuffer.append("Name: " +name + "\n");
		strBuffer.append("ClassName: " + className + "\n");
		strBuffer.append("MaxElementsInMemory: " + maxElementsInMemory + "\n");
		strBuffer.append("idleTime: " +idleTime + "\n");
		strBuffer.append("timeToLive: " + timeToLive + "\n");
		strBuffer.append("eternal: " + eternal + "\n");
		strBuffer.append("overFlowToDisk: " + overFlowToDisk + "\n");
		strBuffer.append("status: " + status + "\n");
		strBuffer.append("objectSize: " + objectSize + "\n");
		strBuffer.append("memoryHits: " + memoryHits + "\n");
		strBuffer.append("diskHits: " + diskHits + "\n");
		strBuffer.append("expiredMisses: " + expiredMisses +"\n");
		strBuffer.append("notFoundMisses: " + notFoundMisses + "\n");
		strBuffer.append("memorySizeCount: " + memorySizeCount + "\n");
		strBuffer.append("diskSizeCount: " + diskSizeCount + "\n");
		
		return strBuffer.toString();
	}
}
