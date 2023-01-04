; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 2, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  store i32 3, i32* %pointer1, align 4
  %pointer2 = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  %a3 = load i32, i32* %pointer2, align 4
  ret i32 %a3
}
