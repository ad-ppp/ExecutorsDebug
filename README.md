## Executor 源码调试。 target sdk: 29
## 记一次Crash的时候记录线程的使用情况，dump如下几个目录
- /proc/sys/kernel/threads-max； 一般情况下，permission deny
- /proc/$pid/task;      线程使用情况
- /proc/$pid/limits;    对进程的限制情况,可以查看最大文件打开数等等。
- /proc/$pid/fd         打开文件的情况

参考： [不可思议的OOM](https://juejin.im/entry/59f7ea06f265da43143ffee4)

---

Log

```
2020-05-04 17:41:43.643 12628-12628/com.jacky.executors.debug D/MyApplication: Analyze file:/proc/sys/kernel/threads-max
    isFile:false
2020-05-04 17:41:43.644 12628-12628/com.jacky.executors.debug W/System.err:     at com.jacky.executors.debug.MyApplication.dumpFile(MyApplication.java:110)
2020-05-04 17:41:43.644 12628-12628/com.jacky.executors.debug W/System.err:     at com.jacky.executors.debug.MyApplication.onCreate(MyApplication.java:22)
2020-05-04 17:41:43.645 12628-12628/com.jacky.executors.debug D/MyApplication: Dump file error :/proc/sys/kernel/threads-max (Permission denied)
2020-05-04 17:41:43.646 12628-12628/com.jacky.executors.debug D/MyApplication: task:
    Analyze file:/proc/12628/task
    isFile:false
    Dir list files length:12
2020-05-04 17:41:52.025 12628-12628/com.jacky.executors.debug D/MyApplication: start dump threads-max,/proc/sys/kernel/threads-max
2020-05-04 17:41:52.025 12628-12628/com.jacky.executors.debug D/MyApplication: Analyze file:/proc/sys/kernel/threads-max
    isFile:false
2020-05-04 17:41:52.025 12628-12628/com.jacky.executors.debug D/MyApplication: end dump threads-max
2020-05-04 17:41:52.025 12628-12628/com.jacky.executors.debug D/MyApplication: start dump limits,/proc/12628/limits
2020-05-04 17:41:52.078 12628-12628/com.jacky.executors.debug D/MyApplication: Limit                     Soft Limit           Hard Limit           Units     
    Max cpu time              unlimited            unlimited            seconds   
    Max file size             unlimited            unlimited            bytes     
    Max data size             unlimited            unlimited            bytes     
    Max stack size            8388608              unlimited            bytes     
    Max core file size        0                    unlimited            bytes     
    Max resident set          unlimited            unlimited            bytes     
    Max processes             4808                 4808                 processes 
    Max open files            32768                32768                files     
    Max locked memory         65536                65536                bytes     
    Max address space         unlimited            unlimited            bytes     
    Max file locks            unlimited            unlimited            locks     
    Max pending signals       4808                 4808                 signals   
    Max msgqueue size         819200               819200               bytes     
    Max nice priority         40                   40                   
    Max realtime priority     0                    0                    
    Max realtime timeout      unlimited            unlimited            us        
2020-05-04 17:41:52.078 12628-12628/com.jacky.executors.debug D/MyApplication: end dump limits
2020-05-04 17:41:52.079 12628-12628/com.jacky.executors.debug D/MyApplication: start dump pds,/proc/12628/fd
2020-05-04 17:41:52.079 12628-12628/com.jacky.executors.debug D/MyApplication: 打开fd的个数38
2020-05-04 17:41:52.079 12628-12628/com.jacky.executors.debug D/MyApplication: end dump pds
2020-05-04 17:41:52.079 12628-12628/com.jacky.executors.debug D/MyApplication: start dump status,/proc/12628/status
2020-05-04 17:41:52.133 12628-12628/com.jacky.executors.debug D/MyApplication: Name:	executors.debug
    State:	R (running)
    Tgid:	12628
    Ngid:	0
    Pid:	12628
    PPid:	1759
    TracerPid:	0
    Uid:	10070	10070	10070	10070
    Gid:	10070	10070	10070	10070
    FDSize:	64
    Groups:	9997 20070 50070 
    NStgid:	12628
    NSpid:	12628
    NSpgid:	1759
    NSsid:	0
    VmPeak:	 4149248 kB
    VmSize:	 4148192 kB
    VmLck:	       0 kB
    VmPin:	       0 kB
    VmHWM:	   97928 kB
    VmRSS:	   70904 kB
    VmData:	 2852352 kB
    VmStk:	    8192 kB
    VmExe:	      20 kB
    VmLib:	  112356 kB
    VmPTE:	    6644 kB
    VmPMD:	      16 kB
    VmSwap:	       0 kB
    HugetlbPages:	       0 kB
    Threads:	2492
    SigQ:	0/4808
    SigPnd:	0000000000000000
    ShdPnd:	0000000000000000
    SigBlk:	0000000000001204
    SigIgn:	0000000000000001
    SigCgt:	00000006400084f8
    CapInh:	0000000000000000
    CapPrm:	0000000000000000
    CapEff:	0000000000000000
    CapBnd:	0000000000000000
    CapAmb:	0000000000000000
    Seccomp:	2
    Cpus_allowed:	f
    Cpus_allowed_list:	0-3
    voluntary_ctxt_switches:	9963
    nonvoluntary_ctxt_switches:	302
2020-05-04 17:41:52.133 12628-12628/com.jacky.executors.debug D/MyApplication: end dump status
2020-05-04 17:41:52.133 12628-12628/com.jacky.executors.debug D/MyApplication: start dump task,/proc/12628/task
2020-05-04 17:41:52.157 12628-12628/com.jacky.executors.debug D/MyApplication: Analyze file:/proc/12628/task
    isFile:false
    Dir list files length:2492
2020-05-04 17:41:52.157 12628-12628/com.jacky.executors.debug D/MyApplication: end dump task
```