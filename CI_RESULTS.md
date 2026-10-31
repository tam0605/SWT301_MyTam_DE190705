# CI Exercise 1–2 tại repository nộp bài

Repository: https://github.com/tam0605/SWT301_MyTam_DE190705 ; nhánh main.

MathUtil: math-util/, workflow maven.yml. Sales Management: sales-management/, workflow sales-ci.yml. JDK 17, Maven clean verify, JAR và test evidence được upload bằng GitHub Actions. Nguồn Sales trùng bản Lab2 hiện có; không tạo project Sales trùng lặp.

Bản trước chuyển repository đã xác minh MathUtil 13/13 và Sales 42/42 PASS. Cả hai workflow tại repository mới đã hoàn tất SUCCESS trên commit triển khai ce0e86f. Các run dưới đây thuộc repository mới, có artifact JAR và test-evidence chưa hết hạn tại lúc kiểm tra. Phạm vi chuyển chỉ Exercise 1–2, không Exercise 3.

## GitHub Actions tại repository mới

| Workflow | Kết quả | Run thật |
|---|---|---|
| Sales Management CI | SUCCESS | [Run 41222154612](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/runs/41222154612) |
| MathUtil CI | SUCCESS | [Run 41222154570](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/runs/41222154570) |

Tải JAR: Actions → chọn run → Artifacts → math-util-1.0-SNAPSHOT hoặc sales-management-1.0-SNAPSHOT. Commit tài liệu tiếp theo ghi các liên kết đã xác minh, không thay mã nguồn/workflow đã chạy.
