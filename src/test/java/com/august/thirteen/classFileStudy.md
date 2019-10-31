https://chiclaim.blog.csdn.net/article/details/101778619

位bit 字节Byte(8bit) 字

class 字节码文件剖析
class 字节码文件是由一组以 8 位字节为基础单位的二进制流，各个数据项严格按照顺序紧凑地排列在字节码文件中，中间没有任何分隔符，
这使得整个 class 文件存储的内容几乎全部是程序运行的必要数据。

magic  cafe babe
minor_version & major_version    45 开始   JDK 版本是 1.8 所以对应主版本就是 52(45+7)
constant_pool_count    constant_pool_count 是从 1 开始数的，比如 constant_pool 有 45 个常量，那么 constant_pool_count 就等于 46
constant_pool(常量池)是 class 字节码文件中占用最大数据项之一。
       常量池主要存放两大类的常量：
       字面量(Literal)      字面量类似于 Java 中的常量，如字符串，数字等
       符号引用(Symbolic)   主要包括 3 类常量：类和接口的全限定名  字段的名称和描述符   方法的名称和描述符
       常量池中的数据项都是表(table)，也就是说都是复杂类型的。为了区分不同的数据项的类型，每个数据项都有一个 tag 属性
access_flags    用于描述类或接口的访问权限和属性
this_class      用于描述当前类的 class 文件，它指向常量池中的 Class 数据项
super_class     用于描述类或接口的父类，它指向常量池中的 Class 数据项。需要注意的是如果一个接口继承了另一个接口，该接口的父类是 Object，而不是继承的这个类
interfaces      用于描述类实现了哪些接口，因为可以实现多个接口，所以 interfaces 是个不定长的数组，所以 interfaces 前面需要放置 interfaces_count
fields_count & fields    fields 用来描述类中的字段的的，因为 field 的数量也是不确定的，所以 fields 前面需要放置 fields_count
methods_count & methods  method_info 用于描述类中的方法，一个类的方法数不确定的，所有需要在前面放置 methods_count
attributes_count & attributes   attribute_info 可用于 ClassFile, field_info, method_info 和 Code_attribute 等数据结构
        max_stack 是 操作数栈 深度的最大值，在方法执行的时候， 操作数栈都不会超过这个深度，虚拟机根据这个值来分配 栈帧 中的操作数栈深度
        max_locals 是 局部变量表 所需要的存储空间。max_locals 的单位是 Slot，Slot 是虚拟机为局部变量分配内存所用的最小单位。
        对于 4 字节类型的数据，每个局部变量只占用 1 个 Slot，占用 8 字节数据类型占用两个 Slot。

字节码在对开发的指导意义
1.内存泄漏   并不是有了内部类就一定会导致内存泄漏。当内部类的实例生命周期比外部类实例要长，才会导致内存泄漏。


“加载” 这个动作实际上是类生命周期的某一个阶段。一个类从被加载到虚拟机内存到卸载出内存，它的生命周期为：
加载、验证、准备、解析、初始化、使用、卸载 7个阶段。其中验证、准备、解析这 3 个阶段统称为“连接”

验证
先从 class 字节码文件的结构开始验证，然后验证是否符合语言规范，接着验证类里的方法是否合法，最后验证符号引用是否合法，
从而保证后面的解析能够顺序进行。可见这 4 个验证过程是一个从浅入深的过程。
准备
准备阶段是为类变量（静态变量）分配内存并设置变量初始值的阶段，这些变量使用的内存都将在 方法区 中进行分配
解析
解析阶段是虚拟机将常量池内的 符号引用 替换为 直接引用 的过程。

我们可以通过 -XX:+TraceClassLoading 来监控类的加载情况
查看 bytecodeInterpreter.cpp 文件的时候，发现通过 -XX:+TraceClassResolution 可以跟踪类的解析情况。