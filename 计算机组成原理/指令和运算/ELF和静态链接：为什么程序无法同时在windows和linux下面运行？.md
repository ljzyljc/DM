###ELF和静态链接：为什么程序无法同时在windows和linux下面运行？

实际上，“C语言代码 - 汇编代码 - 机器码”这个过程，在我们的计算机上进行的时候是分成两部分组成的。

第一部分由编译（Compile），汇编（Assemble）以及链接（Link）三个阶段组成。在这三个阶段完成之后，我们就生成了一个可执行文件。

汇编文件：add_lib.o以及link_example.o并不是一个可执行文件，而是目标文件。只有通过链接器把多个目标文件以及调用函数库链接起来，我们才能得到一个可执行文件。

第二部分，我们通过装载器（Loader）把可执行文件装载（Load）到内存中。CPU从内存中读取指令和数据，来开始真正执行程序。

<img src="../../image/编译汇编链接装载.jpg">

**ELF格式和链接：理解链接过程**

程序最终是通过装载器编程指令和数据的，所以其实我们生成的可执行代码也不仅仅是一条条的指令。

可执行文件如下：

```link_example:     file format elf64-x86-64
Disassembly of section .init:
...
Disassembly of section .plt:
...
Disassembly of section .plt.got:
...
Disassembly of section .text:
...

 6b0:   55                      push   rbp
 6b1:   48 89 e5                mov    rbp,rsp
 6b4:   89 7d fc                mov    DWORD PTR [rbp-0x4],edi
 6b7:   89 75 f8                mov    DWORD PTR [rbp-0x8],esi
 6ba:   8b 55 fc                mov    edx,DWORD PTR [rbp-0x4]
 6bd:   8b 45 f8                mov    eax,DWORD PTR [rbp-0x8]
 6c0:   01 d0                   add    eax,edx
 6c2:   5d                      pop    rbp
 6c3:   c3                      ret    
00000000000006c4 <main>:
 6c4:   55                      push   rbp
 6c5:   48 89 e5                mov    rbp,rsp
 6c8:   48 83 ec 10             sub    rsp,0x10
 6cc:   c7 45 fc 0a 00 00 00    mov    DWORD PTR [rbp-0x4],0xa
 6d3:   c7 45 f8 05 00 00 00    mov    DWORD PTR [rbp-0x8],0x5
 6da:   8b 55 f8                mov    edx,DWORD PTR [rbp-0x8]
 6dd:   8b 45 fc                mov    eax,DWORD PTR [rbp-0x4]
 6e0:   89 d6                   mov    esi,edx
 6e2:   89 c7                   mov    edi,eax
 6e4:   b8 00 00 00 00          mov    eax,0x0
 6e9:   e8 c2 ff ff ff          call   6b0 <add>
 6ee:   89 45 f4                mov    DWORD PTR [rbp-0xc],eax
 6f1:   8b 45 f4                mov    eax,DWORD PTR [rbp-0xc]
 6f4:   89 c6                   mov    esi,eax
 6f6:   48 8d 3d 97 00 00 00    lea    rdi,[rip+0x97]        # 794 <_IO_stdin_used+0x4>
 6fd:   b8 00 00 00 00          mov    eax,0x0
 702:   e8 59 fe ff ff          call   560 <printf@plt>
 707:   b8 00 00 00 00          mov    eax,0x0
 70c:   c9                      leave  
 70d:   c3                      ret    
 70e:   66 90                   xchg   ax,ax
...
Disassembly of section .fini:
...
```

可以发现，可执行代码dump出来内容，和之前的目标文件代码长得差不多，但是长了很多。因为在Linux下，可执行文件和目标文件所使用的的都是一种叫ELF的文件格式，中文名字叫做**可执行与可链接文件格式**，这里面不仅仅存放了编译成的汇编指令，还保留了很多别的数据。很多变量名称，函数名称存储在ELF文件里面，存储在一个叫做**符号表**的位置里。符号表相当于一个地址簿，把名字和地址关联了起来。

我们先只关注我们的add以及main函数相关的部分。你会发现，这里面，main函数里调用的跳转地址，不再是下一条指令的地址了，而是add函数的入口地址了，这就是EFL格式和链接器的功劳。

<img src= "../../image/EFL文件格式.jpg">

链接器会扫描所有输入的目标文件，然后把所有符号表里的信息收集起来，构成一个全局的符号表。然后再根据重定位表，把所有不确定要跳转地址的代码，根据符号表里面存储的地址，进行一次修正。最后，把所有目标文件的对应段进行一次合并，变成了最终的可执行代码。这也是为什么，可执行文件里面的函数调用的地址都是正确的。

在链接器把程序变成可执行文件之后，要装载器去执行程序就容易多了。装载器不再需要考虑地址跳转的问题，只需要解析ELF文件，把对应的指令和数据，加载到内存里面供CPU执行就可以了。

总结：为什么同样一个程序，在Linux下可以执行而在Windows下不能执行了。其中一个非常重要的原因就是，两个操作系统下可执行文件的格式不一样。

Windows下可执行文件格式是一种叫做PE的文件格式，Linux下的装载器只能解析ELF格式而不能解析PE格式。

Linux下著名的开源项目Wine，就是通过兼容PE格式的装载器，Windows里面也提供了WSL，可以解析和加载ELF格式的文件。

不同的文件之间既有分工，又能通过静态链接来合作，编程一个可执行的程序。

对于ELF格式的文件，为了能够实现这样一个静态链接的机制，里面不只是简单罗列了程序所需要执行的指令，还会包括链接所需要的重定位表和符号表。

