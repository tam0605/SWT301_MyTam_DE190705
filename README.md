# SWT301 — CI Maven: Exercise 1 và Exercise 2

[![MathUtil CI](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/workflows/maven.yml?query=branch%3Amain)
[![Sales Management CI](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/workflows/sales-ci.yml/badge.svg?branch=main)](https://github.com/tam0605/SWT301_MyTam_DE190705/actions/workflows/sales-ci.yml?query=branch%3Amain)

Phạm vi bài nộp: Exercise 1 MathUtil và Exercise 2 Sales Management trong đề CI Maven. Không có Exercise 3, Jest hoặc Report5 trong gói CI này. Nhánh nộp: `main` của repository cá nhân tam0605/SWT301_MyTam_DE190705. Các bài Lab2 đã có trong repository được giữ lại; phần CI lần này chỉ gồm MathUtil và Sales Management.


[Hướng dẫn CI Exercise 1–2](README_CI_EX1_EX2.md) · [Kết quả CI](CI_RESULTS.md)

---

# Bộ bài nộp SWT301 Slot 6

Hoàn thành hai bài: Sales Management System và Lab 2 AccountService.

- Unit test.xlsx: ma trận test case theo mẫu, TestReport 75 ca và BugReport.
- Bao cao kiem thu.md: phân tích, kết quả trước/sau, giả định và hướng dẫn chạy.
- sales-management: project Maven, Product, SalesService, JUnit 5 và JaCoCo.
- account-registration: project Maven, AccountService, test-data.csv và JUnit 5.
- Trong evidence/after của từng project: kết quả CSV, Surefire và ảnh JaCoCo thực tế.
- Trong sales-management/evidence/before: source lỗi và bằng chứng 13 FAIL trước sửa.
- target/site/jacoco/index.html: báo cáo coverage đầy đủ.

Kết quả cuối: Sales 42/42 PASS; AccountService 33/33 PASS. Cả hai đạt 100% line và branch coverage.

Chạy lại trong mỗi project: mvn clean verify (JDK 17 trở lên và Maven).
Có thể dùng mvn test rồi mvn jacoco:report như đề bài.

Trước khi nộp, điền tên/MSSV trong Excel. Đọc lưu ý B06: đề yêu cầu ít nhất 6 lỗi nhưng code có 5 lỗi nghiệp vụ trực tiếp; lỗi thứ sáu là giả định validation total âm, được ghi rõ để trao đổi với giảng viên.

Hướng dẫn có phần thuyết trình nhóm về framework khác JUnit; phần đó tách riêng, không nằm trong hai bài đã lựa chọn.
