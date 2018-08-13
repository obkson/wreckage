# The Wreckage Records Benchmarking Library

## Generated Source Code Samples

#### Creation Time against Record Size.
For structurally typed records, Wreckage generates JMH Benchmarks analoguous to the following:

```
// Mutable global state to prevent constant folding:
var x = 1 

// Benchmarked functions, returning the result to
// JMH's Blackhole to prevent dead code elimination:
def create_f1 = Record("f1", x)                
def create_f2 = Record("f1", x) + ("f2", 2)
...
def create_f32 = Record("f1", x) + ("f2", 2) + ... + f("f32", 32)
```

The corresponding code for nominally typed case classes is:

```
// Need to declare every nominal type:
case class Rec1(f1: Int)
case class Rec2(f1: Int, f2: Int)
...
case class Rec32(f1: Int, f2: Int, ..., f32: Int)

// Mutable global state:
var x = 1

// Benchmarked functions:
def create_f1 = Rec1(f1=x)
def create_f2 = Rec2(f1=x, f2=2)
...
def create_f32 = Rec32(f1=x, f2=2, ..., f32=32)
```

#### Update Time against Record Size.
For structurally typed records, Wreckage generates JMH Benchmarks analoguous to the following:

```
// Mutable global state:
var r_1 = Record("f1", 1)
var r_20 = Record("f1", 1) + ("f2", 2) + ... + ("f20", 20)
...
var r_240 = Record("f1", 1) + ("f2", 2) + ... + ("f240", 240)

// Benchmarked functions:
def update_f1 = r_1 + ("f1", (r_1.f1+1))
def update_f20 = r_20 + ("f20", (r_20.f20+1))
...
def update_f240 = r_240 + ("f240", (r_240.f240+1))
```

The corresponding code for nominally typed case classes is:

```
// Need to declare every nominal type:
case class Rec1(f1: Int)
case class Rec1(f1: Int, f2: Int, ..., f20: Int)
...
case class Rec240(f1: Int, f2: Int, ..., f240: Int)

// Mutable global state:
var r_1 = Rec1(f1=1)
var r_20 = Rec20(f1=1, f2=2, ..., f20=20)                         
...
var r_240 = Rec240(f1=1, f2=2, ..., f240=240)

// Benchmarked functions:
def update_f1 = r_1.copy(f1=r_1.f1+1)
def update_f20 = r_20.copy(f20=r_20.f20+1)
...
def update_f240 = r_240.copy(f240=r_240.f240+1)
```

#### Access Time against Field Index.

For structurally typed records, Wreckage generates JMH Benchmarks analoguous to the following:

```
// Mutable global state:
var r = Record("f1", 1) + ("f2", 2) + ... + ("f32", 32)

// Benchmarked functions:
def access_f1 = r.f1
def access_f2 = r.f2
...
def access_f32 = r.f32
```

The corresponding code for nominally typed case classes is:

```
// Need to declare every nominal type:
case class Rec32(f1: Int, f2: Int, ..., f32: Int)

// Mutable global state:
var r = Rec32(f1=1, f2=2, ..., f32=32)

// Benchmarked functions:
def access_f1 = r.f1
def access_f2 = r.f2
...
def access_f32 = r.f32
```

#### Access Time against Record Size.

For structurally typed records, Wreckage generates JMH Benchmarks analoguous to the following:

```
// Mutable global state:
var r_1 = Record("f1", 1)                
var r_2 = Record("f1", 1) + ("f2", 2)
...
var r_32 = Record("f1", 1) + ("f2", 2) + ... + ("f32", 32)

// Benchmarked functions:
def access_f1 = r_1.f1
def access_f2 = r_2.f2
...
def access_f32 = r_32.f32
```

The corresponding code for nominally typed case classes is:

```
// Need to declare every nominal type:
case class Rec1(f1: Int)
case class Rec2(f1: Int, f2: Int)
...
case class Rec32(f1: Int, ..., f32: Int)

// Mutable global state:
var r_1 = Rec1(f1=1)
var r_2 = Rec2(f1=1, f2=2)
...
var r_32 = Rec32(f1=1, f2=2, ..., f32=32)

// Benchmarked functions:
def access_f1 = r_1.f1
def access_f2 = r_2.f2
...
def access_f32 = r_32.f32
```

#### Access Time against Degree of Polymorphism.

For structurally typed records, Wreckage generates JMH Benchmarks analoguous to the following:

```
// Mutable global state:
val rs = Array[Record{ val g: Int ](                 
  Record("g", 1) + ("f2", 2) + ... + ("f32", 32),
  Record("f1", 1) + ("g", 2) + ... + ("f32", 32),
  ...
  Record("f1", 1) + ("f2", 2) + ... + ("g", 32)
)
var i = -1

// Benchmarked functions (iterated):
def poly_deg1 = { i = (i + 1) % 1; rs(i).g 
def poly_deg2 = { i = (i + 1) % 2; rs(i).g 
...
def poly_deg32 = { i = (i + 1) % 32; rs(i).g 
```

The corresponding code for nominally typed case classes is:

```
// Need to declare the nominal types,
// including common base type
class Base(val g: Int)
case class SubType1(
  override val g: Int, f2: Int, ... f32: Int
) extends Base(g)
case class SubType2(
  f1: Int, override val g: Int, ... f32: Int
) extends Base(g)
...
case class SubType32(
  f1: Int, f2: Int, ..., override val g: Int
) extends Base(g)

// Mutable global state
val rs = Array[Base](           
  SubType1(g=1, f2=2, ..., f32=32),             
  SubType2(f1=1, g=2, ..., f32=32),
  ...
  SubType32(f1=1, f2=2, ..., g=32)
)
var i = -1

// Benchmarked functions (iterated):
def poly_deg1 = { i = (i + 1) % 1; rs(i).g }
def poly_deg2 = { i = (i + 1) % 2; rs(i).g }
...
def poly_deg32 = { i = (i + 1) % 32; rs(i).g }
```
