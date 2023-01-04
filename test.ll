; ModuleID = 'moudle'
source_filename = "moudle"

define void @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 2, i32* %a, align 4
  %a1 = load i32, i32* %a, align 4
  %result = add i32 %a1, 3
}
