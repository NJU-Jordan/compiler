; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @f(i32 %0) {
fEntry:
  %a = alloca i32, align 4
  store i32 %0, i32* %a, align 4
  %a1 = load i32, i32* %a, align 4
  %b = alloca i32, align 4
  store i32 %a1, i32* %b, align 4
  %b2 = load i32, i32* %b, align 4
  ret i32 %b2
}

define i32 @main() {
mainEntry:
  %x = alloca i32, align 4
  store i32 2, i32* %x, align 4
  %x1 = load i32, i32* %x, align 4
  %f = call i32 @f(i32 %x1)
  ret i32 %f
}
