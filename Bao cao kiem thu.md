# Báo cáo thực hành Unit Test SWT301

Bài làm gồm Sales Management System và Lab 2 đăng ký tài khoản. Kết quả dưới đây được lấy từ lần chạy Maven thực tế ngày 06/10/2026 theo giờ Việt Nam. Không thay đổi giá trị Expected để làm test PASS.

## Kết quả thực thi

| Dự án | Tổng test | PASS | FAIL | Line coverage | Branch coverage |
|---|---:|---:|---:|---:|---:|
| Sales trước sửa | 42 | 29 | 13 | Không dùng làm kết quả cuối | Không dùng làm kết quả cuối |
| Sales sau sửa | 42 | 42 | 0 | 100% (31/31) | 100% (34/34) |
| Lab 2 AccountService | 33 | 33 | 0 | 100% (5/5) | 100% (14/14) |

Coverage chỉ tính các lớp production trong từng dự án; không tính lớp test hoặc lớp ghi bằng chứng. Branch coverage của AccountService không đo toàn bộ trạng thái bên trong bộ máy regex.

## Task 1 phân tích lỗi SalesService

Mã trong đề có 5 lỗi nghiệp vụ xác định trực tiếp. Để xử lý yêu cầu “ít nhất 6 lỗi”, B06 được trình bày riêng là thiếu validation theo giả định tổng tiền không âm, không khẳng định đề đã quy định rõ. Nếu giảng viên chỉ chấp nhận lỗi thuộc yêu cầu trực tiếp, cần trao đổi về sự lệch giữa số lỗi trong code và số lỗi yêu cầu. Không tách một nguyên nhân thành nhiều bug để đủ số lượng.

| Bug ID | Method | Mô tả | Input | Expected | Actual trước sửa | Test phát hiện | Cách sửa | Cơ sở |
|---|---|---|---|---|---|---|---|---|
| B01 | calculateSubtotal | Cộng price + quantity thay vì nhân | price=500; quantity=3 | 1500 | 503 | S01, S02, S03 | Đổi + thành * | Yêu cầu trực tiếp |
| B02 | calculateDiscount | Sai tỷ lệ trong khoảng 1000 ≤ subtotal < 5000 | subtotal=1000 | 50 | 100 | D03, D04 | Đổi 10% thành 5% | Yêu cầu trực tiếp |
| B03 | calculateShippingFee | Dùng ≤ 2000 khiến mốc 2000 vẫn tính phí | subtotal=2000 | 0 | 50 | H03 | Đổi <= thành < | Yêu cầu trực tiếp |
| B04 | calculateTotal | Cộng discount vào tổng thay vì trừ | price=500; quantity=3 | 1475 | 553 | T01–T05 | subtotal - discount + shipping | Yêu cầu trực tiếp; Actual chịu cả B01/B02 |
| B05 | classifyCustomer | Dùng ≤ 10000 khiến total=10000 thuộc GOLD | total=10000 | VIP | GOLD | C07 | Đổi <= thành < | Yêu cầu trực tiếp |
| B06 | classifyCustomer | Thiếu kiểm tra total âm | total=-1 | IllegalArgumentException | REGULAR | C09 | Thêm guard total < 0 | Giả định bổ sung; đề chưa quy định trực tiếp |

Actual 553 của B04 là kết quả chạy cả luồng trên source ban đầu, chịu ảnh hưởng cả lỗi tính subtotal và discount. Nguyên nhân B04 được xác định từ câu lệnh `return subtotal + discount + shipping`; phép cộng discount là sai độc lập với các lỗi upstream.

## Task 2 và Task 3 thiết kế kiểm thử

Áp dụng phân vùng tương đương và giá trị biên. Oracle là các kết quả cố định tính theo đề, không dùng chính method cần kiểm thử để tính Expected. So sánh double với delta 0.001 theo ví dụ đề bài; không tự thêm quy tắc làm tròn tiền.

| Method | Số lần chạy | Nội dung chính | Tối thiểu theo đề |
|---|---:|---|---:|
| calculateSubtotal | 4 | 3 giá trị hợp lệ và product=null | 3 |
| calculateDiscount | 9 | 0, 999.99, 1000, 4999.99, 5000, 9999.99, 10000, 20000 và -1 | 6 |
| calculateShippingFee | 5 | 0, 1999.99, 2000, 2000.01 và -1 | 3 |
| calculateTotal | 6 | Các mức giảm giá, có/không có phí ship và product=null | 2 |
| classifyCustomer | 9 | 0, các biên 1000/5000/10000 và -1 theo giả định | 4 |
| Product | 9 | Constructor, getters; ID/name null hoặc rỗng; price/quantity bằng 0 hoặc âm | Bổ sung |

`SalesServiceTest.java` dùng nhiều `@ParameterizedTest` với `@CsvSource`. Có 5 trường hợp ngoại lệ SalesService và 8 trường hợp Product; mỗi trường hợp gọi `assertThrows` thông qua helper TestEvidence. Helper chỉ ghi Actual và thực hiện assertion, không chứa nghiệp vụ. Mỗi test được khởi tạo service riêng bằng `@BeforeEach`.

## Task 4 bằng chứng trước và sau sửa

Trước sửa, FAIL: S01, S02, S03, D03, D04, H03, T01, T02, T03, T04, T05, C07 và C09. Trong đó C09 kiểm tra giả định B06, còn 12 FAIL thuộc yêu cầu trực tiếp. Source ban đầu và báo cáo Surefire được lưu trong `sales-management/evidence/before`.

Sau sửa, dùng lại cùng các giá trị Expected và cùng test suite: 42 PASS, 0 FAIL, 0 ERROR, 0 SKIPPED. Source đã sửa nằm trong `sales-management/src/main/java/vn/edu/fpt/SalesService.java`; bằng chứng cuối nằm trong `evidence/after`. File Excel giữ cả Actual trước và sau sửa trong sheet TestReport.

## Task 5 JaCoCo

Hai dự án cấu hình JaCoCo prepare-agent, report và check. `mvn verify` tự tạo báo cáo và chặn build nếu line coverage dưới 90% hoặc branch coverage dưới 80%. Có thể chạy đúng hai lệnh trong đề `mvn test` rồi `mvn jacoco:report` để tạo report; dùng `mvn verify` để kiểm tra luôn ngưỡng coverage.

Ảnh chụp JaCoCo kèm theo được tạo từ trang HTML report thực tế. Có ảnh tổng dự án và ảnh lớp SalesService. Report HTML đầy đủ nằm tại `target/site/jacoco/index.html` của từng dự án.

## Task 6 Excel test case và test report

File `Unit test.xlsx` sử dụng ma trận mẫu cho từng method, ghi đủ điều kiện đầu vào, Expected, Actual, ngoại lệ, loại N/A/B, trạng thái, ngày chạy và Bug ID. Sheet TestReport có bảng từng lần chạy, gồm cả kết quả trước/sau sửa; sheet BugReport chứa Task 1.

Ô x đánh dấu điều kiện áp dụng. N là Normal, A là Abnormal, B là Boundary. Giá trị null, chuỗi rỗng và khoảng trắng được ghi riêng. Mẫu có cột test rất hẹp và công thức tổng hợp tham chiếu sai hàng: phần trình bày được mở rộng vừa đủ để đọc; giữ các nhãn và vùng xám, sửa các công thức tổng hợp liên quan để đếm hàng Result 48 và Type 47. Ô Lines of code sử dụng số dòng thực thi của method từ JaCoCo, không tính comment/dòng trắng. Lack of test cases là số ca còn thiếu so với tối thiểu trong đề; Product và Lab 2 không có số tối thiểu nên để 0. Đây là hiệu chỉnh mẫu có chủ đích nhằm tránh thống kê sai. Người nộp tự điền tên và MSSV; không gán danh tính hoặc tên người thực thi giả.

## Lab 2 AccountService

Giữ nguyên `src/test/resources/test-data.csv` với header `username,password,email,expected` và 5 dòng dữ liệu của đề. Test `@CsvFileSource(resources="/test-data.csv", numLinesToSkip=1)` thực thi cả 5 dòng. Bổ sung 10 trường hợp registerAccount và 18 trường hợp isValidEmail: tổng 33 test.

Password “lớn hơn 6 ký tự” được hiểu là từ 7 ký tự, không phải từ 6. Username null/rỗng/toàn khoảng trắng bị từ chối, phù hợp dòng username rỗng trong CSV. Đăng ký trả về boolean; không tự thêm database, trùng tài khoản hoặc exception vì đề chưa yêu cầu.

Đề chưa định nghĩa toàn bộ tiêu chuẩn email. Phạm vi triển khai là email ASCII thông dụng: một ký tự @; phần local gồm chữ/số, `_ + % -` và các dấu chấm ngăn cách không rỗng; domain có dấu chấm, nhãn không bắt đầu/kết thúc bằng dấu gạch ngang và TLD là ít nhất hai chữ cái. Không kiểm tra DNS, không hỗ trợ toàn bộ dạng email theo RFC hay email Unicode, không tự trim email. Các quy tắc này là giả định được công bố, không coi là yêu cầu nguyên văn. Password tính theo String.length() như bài Java cơ bản; không thêm quy tắc độ mạnh hoặc trim password.

## Môi trường và cách chạy

Môi trường chạy thực tế: Windows, JDK 24.0.1, Maven 3.9.16; compiler target Java 17 theo tài liệu hướng dẫn. JUnit 5.10.2, Surefire 3.2.5, JaCoCo 0.8.12. Code dùng được với JDK 17 trở lên.

1. Mở thư mục `sales-management` bằng NetBeans hoặc terminal và chạy `mvn clean verify`.
2. Mở `account-registration` và chạy `mvn clean verify`.
3. Mở `target/site/jacoco/index.html` để xem report. Các file CSV Actual được tạo lại ở target mỗi lần chạy.
4. Điền thông tin cá nhân trong Excel trước khi nộp. Ngày 06/10/2026 là ngày thực thi lần này; khi chạy lại, cập nhật ngày theo lần chạy mới.

Dự án MathUtil và các tệp đề gốc được giữ nguyên. Bài thuyết trình nhóm về framework khác JUnit xuất hiện riêng trong tài liệu hướng dẫn; không nằm trong phạm vi hai bài Sales và Lab 2 đã lựa chọn.

Coverage 100% xác nhận các dòng và nhánh đã được thực thi trong phạm vi code; không khẳng định không còn mọi lỗi. Các giá trị NaN/Infinity, overflow subtotal và toàn bộ biến thể email RFC không được đề quy định và không thuộc bộ test hiện tại.
