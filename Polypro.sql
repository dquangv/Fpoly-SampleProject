create database Polypro;
go

use Polypro;
go

create table hocvien (
	mahocvien int identity (1, 1) primary key,
	makhoahoc int not null,
	manguoihoc varchar(10) not null,
	diemtrungbinh float default -1,
	unique (makhoahoc, manguoihoc)
);
go

create table nguoihoc (
	manguoihoc varchar(10) primary key not null,
	hovaten nvarchar(50) not null,
	ngaysinh date not null,
	gioitinh bit default 0,
	sodienthoai varchar(15) not null,
	email varchar(50) not null,
	ghichu nvarchar(200),
	manhanvien varchar(10) not null,
	ngaydangky date default getdate(),
	hinhanh varchar(500)
);
go

create table nguoihoc_format (
	manguoihoc varchar(10) primary key not null,
	hovaten nvarchar(50) not null,
	ngaysinh date not null,
	gioitinh bit default 0,
	sodienthoai varchar(15) not null,
	email varchar(50) not null,
	ghichu nvarchar(200),
	manhanvien varchar(10) not null,
	ngaydangky date default getdate(),
	hinhanh varchar(500),
	ngaysinh_format varchar(20),
	ngaydangky_format varchar(20),
);
go

create table chuyende (
	machuyende varchar(10) primary key not null,
	tenchuyende nvarchar(50) unique not null,
	hocphi money default 0,
	thoiluong int default 30,
	hinhlogo varchar(200) not null,
	motachuyende nvarchar(200) not null,
	check (hocphi >= 0 and thoiluong > 0),
);
go

create table khoahoc (
	makhoahoc int identity(1, 1) primary key,
	machuyende varchar(10) not null,
	hocphi money default 0,
	thoiluong int default 0,
	ngaykhaigiang date not null,
	ghichu nvarchar(100),
	manhanvien varchar(10) not null,
	ngaytao date default getdate(),
	check (hocphi >= 0 and thoiluong > 0)
);
go

create table nhanvien (
	manhanvien varchar(10) primary key not null,
	matkhau varchar(20) not null,
	hovaten nvarchar(50) not null,
	vaitro bit default 0,
	email varchar(50),
	SDT varchar(13)
);
go

alter table nguoihoc
add
	constraint fk_nh_nv
	foreign key (manhanvien)
	references nhanvien (manhanvien) on update cascade on delete no action;
go

alter table khoahoc
add 
	constraint fk_kh_cd
	foreign key (machuyende)
	references chuyende (machuyende) on update cascade on delete no action,
	
	constraint fk_kh_nv
	foreign key (manhanvien)
	references nhanvien (manhanvien) on update cascade on delete no action;
go

alter table hocvien
add
	constraint fk_hv_kh
	foreign key (makhoahoc)
	references khoahoc (makhoahoc) on delete cascade on update cascade,
	
	constraint fk_hv_nh
	foreign key (manguoihoc)
	references nguoihoc (manguoihoc) on update cascade on delete no action;
go

create proc sp_ThongKeNguoiHoc
as
begin
	select
		year(ngaydangky) Nam,
		count(*) SoLuong,
		min(ngaydangky)	DauTien,
		max(ngaydangky) CuoiCung
	from nguoihoc
	group by year(ngaydangky)
end;
go

create proc sp_ThongKeDoanhThu (@year int)
as
begin
	select
		tenchuyende ChuyenDe,
		count(distinct kh.makhoahoc) SoKhoaHoc,
		count(hv.mahocvien) SoHocVien,
		sum(kh.hocphi) DoanhThu,
		min(kh.hocphi) ThapNhat,
		max(kh.hocphi) CaoNhat,
		avg(kh.hocphi) TrungBinh
	from khoahoc kh
		join hocvien hv on kh.makhoahoc = hv.makhoahoc
		join chuyende cd on cd.machuyende = kh.machuyende
	where year(ngaykhaigiang) = @year
	group by tenchuyende
end;
go

create proc sp_ThongKeDiem
as
begin
	select
		tenchuyende ChuyenDe,
		count(mahocvien) SoHocVien,
		min(diemtrungbinh) ThapNhat,
		max(diemtrungbinh) CaoNhat,
		avg(diemtrungbinh) TrungBinh
	from khoahoc kh
		join hocvien hv on kh.makhoahoc = hv.makhoahoc
		join chuyende cd on kh.machuyende = cd.machuyende
	group by tenchuyende
end;
go

create proc sp_BangDiem(@makh int)
as
begin
	select
		nh.manguoihoc,
		nh.hovaten,
		hv.diemtrungbinh
	from hocvien hv
		join nguoihoc nh on nh.manguoihoc = hv.manguoihoc
	where hv.makhoahoc = @makh
	order by hv.diemtrungbinh desc
end;
go

/*
create trigger tg_NguoiHoc_AfterInsert
on nguoihoc
after insert
as
begin
	update nh
	set ngaysinh = format(i.ngaysinh, 'dd/MM/yyyy'),
		ngaydangky = format(i.ngaydangky, 'dd/MM/yyyy')
	from nguoihoc nh
	inner join inserted i
	on nh.manguoihoc = i.manguoihoc;
end;
go

create trigger tg_KhoaHoc_AfterInsert
on khoahoc
after insert
as
begin
	update kh
	set ngaykhaigiang = format(i.ngaykhaigiang, 'dd/MM/yyyy'),
		ngaytao = format(i.ngaytao, 'dd/MM/yyyy')
	from khoahoc kh
	inner join inserted i
	on i.makhoahoc = kh.makhoahoc;
end;
go
*/

insert into chuyende values
	(N'JAV01', N'Lập trình Java cơ bản', 300, 90, N'GAME.png', N'JAV01 - Lập trình Java cơ bản'),
	(N'JAV02', N'Lập trình Java nâng cao', 300, 90, N'HTCS.jpg', N'JAV02 - Lập trình Java nâng cao'),
	(N'JAV03', N'Lập trình mạng với Java', 200, 70, N'INMA.jpg', N'JAV03 - Lập trình mạng với Java'),
	(N'JAV04', N'Lập trình desktop với Swing', 200, 70, N'LAYO.jpg', N'JAV04 - Lập trình desktop với Swing'),
	(N'PRO01', N'Dự án với công nghệ MS.NET MVC', 300, 90, N'MOWE.png', N'PRO01 - Dự án với công nghệ MS.NET MVC'),
	(N'PRO02', N'Dự án với công nghệ Spring MVC', 300, 90, N'Subject.png', N'PRO02 - Dự án với công nghệ Spring MVC'),
	(N'PRO03', N'Dự án với công nghệ Servlet/JSP', 300, 90, N'GAME.png', N'PRO03 - Dự án với công nghệ Servlet/JSP'),
	(N'PRO04', N'Dự án với AngularJS & WebAPI', 300, 90, N'HTCS.jpg', N'PRO04 - Dự án với AngularJS & WebAPI'),
	(N'PRO05', N'Dự án với Swing & JDBC', 300, 90, N'INMA.jpg', N'PRO05 - Dự án với Swing & JDBC'),
	(N'PRO06', N'Dự án với WindowForm', 300, 90, N'LAYO.jpg', N'PRO06 - Dự án với WindowForm'),
	(N'RDB01', N'Cơ sở dữ liệu SQL Server', 100, 50, N'MOWE.png', N'RDB01 - Cơ sở dữ liệu SQL Server'),
	(N'RDB02', N'Lập trình JDBC', 150, 60, N'Subject.png', N'RDB02 - Lập trình JDBC'),
	(N'RDB03', N'Lập trình cơ sở dữ liệu Hibernate', 250, 80, N'GAME.png', N'RDB03 - Lập trình cơ sở dữ liệu Hibernate'),
	(N'SER01', N'Lập trình web với Servlet/JSP', 350, 100, N'HTCS.jpg', N'SER01 - Lập trình web với Servlet/JSP'),
	(N'SER02', N'Lập trình Spring MVC', 400, 110, N'INMA.jpg', N'SER02 - Lập trình Spring MVC'),
	(N'SER03', N'Lập trình MS.NET MVC', 400, 110, N'LAYO.jpg', N'SER03 - Lập trình MS.NET MVC'),
	(N'SER04', N'Xây dựng Web API với Spring MVC & ASP.NET MVC', 200, 70, N'MOWE.png', N'SER04 - Xây dựng Web API với Spring MVC & ASP.NET MVC'),
	(N'WEB01', N'Thiết kế web với HTML và CSS', 200, 70, N'Subject.png', N'WEB01 - Thiết kế web với HTML và CSS'),
	(N'WEB02', N'Thiết kế web với Bootstrap', 0, 40, N'GAME.png', N'WEB02 - Thiết kế web với Bootstrap'),
	(N'WEB03', N'Lập trình front-end với JavaScript và jQuery', 150, 60, N'HTCS.jpg', N'WEB03 - Lập trình front-end với JavaScript và jQuery'),
	(N'WEB04', N'Lập trình AngularJS', 250, 80, N'INMA.jpg', N'WEB04 - Lập trình AngularJS');
go

insert into nhanvien values
	(N'QuangVD', '123456', N'Vũ Đăng Quang', 1, 'quangvdps36680@fpt.edu.vn', '0101010101'),
	(N'NgocTTM', '123456', N'Trương Thị Minh Ngọc', 1, 'dquangvu7998@gmail.com', '0202020202'),
	(N'LongVH', '123456', N'Vũ Hoàng Long', 0, 'vudangquang7799@gmail.com', '0303030303');
go

insert into nguoihoc values
	(N'PS01638', N'LỮ HUY CƯỜNG', CAST(0xAF170B00 AS Date), 1, N'0928768265', N'PS01638@fpt.edu.vn', N'0928768265 - LỮ HUY CƯỜNG', N'QuangVD', CAST(0xAF170B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\1.jpg'),
	(N'PS02037', N'ĐỖ VĂN MINH', CAST(0xC6190B00 AS Date), 1, N'0968095685', N'PS02037@fpt.edu.vn', N'0968095685 - ĐỖ VĂN MINH', N'LongVH', CAST(0xC6190B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\2.jpg'),
	(N'PS02771', N'NGUYỄN TẤN HIẾU', CAST(0x2E220B00 AS Date), 1, N'0927594734', N'PS02771@fpt.edu.vn', N'0927594734 - NGUYỄN TẤN HIẾU', N'QuangVD', CAST(0x2E220B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\3.jpg'),
	(N'PS02867', N'NGUYỄN HỮU TRÍ', CAST(0xEB200B00 AS Date), 1, N'0946984711', N'PS02867@fpt.edu.vn', N'0946984711 - NGUYỄN HỮU TRÍ', N'NgocTTM', CAST(0xEB200B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\4.jpg'),
	(N'PS02930', N'TRẦN VĂN NAM', CAST(0xA1240B00 AS Date), 1, N'0924774498', N'PS02930@fpt.edu.vn', N'0924774498 - TRẦN VĂN NAM', N'NgocTTM', CAST(0xA1240B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\5.jpg'),
	(N'PS02979', N'ĐOÀN TRẦN NHẬT VŨ', CAST(0x671C0B00 AS Date), 1, N'0912374818', N'PS02979@fpt.edu.vn', N'0912374818 - ĐOÀN TRẦN NHẬT VŨ', N'NgocTTM', CAST(0x671C0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\6.jpg'),
	(N'PS02983', N'NGUYỄN HOÀNG THIÊN PHƯỚC', CAST(0x681A0B00 AS Date), 1, N'0912499836', N'PS02983@fpt.edu.vn', N'0912499836 - NGUYỄN HOÀNG THIÊN PHƯỚC', N'QuangVD', CAST(0x681A0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\7.jpg'),
	(N'PS02988', N'HỒ HỮU HẬU', CAST(0x311A0B00 AS Date), 1, N'0924984876', N'PS02988@fpt.edu.vn', N'0924984876 - HỒ HỮU HẬU', N'LongVH', CAST(0x311A0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\8.jpg'),
	(N'PS03031', N'PHAN TẤN VIỆT', CAST(0x21160B00 AS Date), 1, N'0924832716', N'PS03031@fpt.edu.vn', N'0924832716 - PHAN TẤN VIỆT', N'LongVH', CAST(0x21160B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\9.jpg'),
	(N'PS03046', N'NGUYỄN CAO PHƯỚC', CAST(0xDE150B00 AS Date), 1, N'0977117727', N'PS03046@fpt.edu.vn', N'0977117727 - NGUYỄN CAO PHƯỚC', N'QuangVD', CAST(0xDE150B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\10.jpg'),
	(N'PS03080', N'HUỲNH THANH HUY', CAST(0x701C0B00 AS Date), 1, N'0916436052', N'PS03080@fpt.edu.vn', N'0916436052 - HUỲNH THANH HUY', N'QuangVD', CAST(0x701C0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\11.jpg'),
	(N'PS03088', N'NGUYỄN HOÀNG TRUNG', CAST(0x24180B00 AS Date), 1, N'0938101529', N'PS03088@fpt.edu.vn', N'0938101529 - NGUYỄN HOÀNG TRUNG', N'LongVH', CAST(0x24180B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\12.jpg'),
	(N'PS03096', N'ĐOÀN HỮU KHANG', CAST(0xAB1B0B00 AS Date), 1, N'0945196719', N'PS03096@fpt.edu.vn', N'0945196719 - ĐOÀN HỮU KHANG', N'QuangVD', CAST(0xAB1B0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\13.jpg'),
	(N'PS03104', N'LÊ THÀNH PHƯƠNG', CAST(0x3E1A0B00 AS Date), 1, N'0922948096', N'PS03104@fpt.edu.vn', N'0922948096 - LÊ THÀNH PHƯƠNG', N'LongVH', CAST(0x3E1A0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\14.jpg'),
	(N'PS03120', N'PHẠM NGỌC NHẬT TRƯỜNG', CAST(0x48230B00 AS Date), 1, N'0994296169', N'PS03120@fpt.edu.vn', N'0994296169 - PHẠM NGỌC NHẬT TRƯỜNG', N'LongVH', CAST(0x48230B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\15.jpg'),
	(N'PS03130', N'ĐẶNG BẢO VIỆT', CAST(0xEF150B00 AS Date), 1, N'0917749344', N'PS03130@fpt.edu.vn', N'0917749344 - ĐẶNG BẢO VIỆT', N'QuangVD', CAST(0xEF150B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\15.jpg'),
	(N'PS03134', N'LÊ DUY BẢO', CAST(0x2E1F0B00 AS Date), 1, N'0926714368', N'PS03134@fpt.edu.vn', N'0926714368 - LÊ DUY BẢO', N'QuangVD', CAST(0x2E1F0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\16.jpg'),
	(N'PS03172', N'NGUYỄN ANH TUẤN', CAST(0xCA180B00 AS Date), 1, N'0920020472', N'PS03172@fpt.edu.vn', N'0920020472 - NGUYỄN ANH TUẤN', N'LongVH', CAST(0xCA180B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\17.jpg'),
	(N'PS03202', N'PHAN QUỐC QUI', CAST(0x741E0B00 AS Date), 1, N'0930649274', N'PS03202@fpt.edu.vn', N'0930649274 - PHAN QUỐC QUI', N'QuangVD', CAST(0x741E0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\18.jpg'),
	(N'PS03203', N'ĐẶNG LÊ QUANG VINH', CAST(0xC4150B00 AS Date), 1, N'0920197355', N'PS03203@fpt.edu.vn', N'0920197355 - ĐẶNG LÊ QUANG VINH', N'QuangVD', CAST(0xC4150B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\19.jpg'),
	(N'PS03205', N'NGUYỄN MINH SANG', CAST(0x5E1D0B00 AS Date), 1, N'0967006218', N'PS03205@fpt.edu.vn', N'0967006218 - NGUYỄN MINH SANG', N'LongVH', CAST(0x5E1D0B00 AS Date), 'C:\Users\Quang\OneDrive - FPT Polytechnic\Desktop\img\bin\20.jpg'),
	(N'PS03222', N'TRẦM MINH MẪN', CAST(0xE71F0B00 AS Date), 1, N'0911183649', N'PS03222@fpt.edu.vn', N'0911183649 - TRẦM MINH MẪN', N'QuangVD', CAST(0xE71F0B00 AS Date), null),
	(N'PS03230', N'NGUYỄN PHẠM MINH HÂN', CAST(0x26250B00 AS Date), 1, N'0983469892', N'PS03230@fpt.edu.vn', N'0983469892 - NGUYỄN PHẠM MINH HÂN', N'LongVH', CAST(0x26250B00 AS Date), null),
	(N'PS03233', N'LƯU KIM HOÀNG DUY', CAST(0x0B1F0B00 AS Date), 1, N'0938357735', N'PS03233@fpt.edu.vn', N'0938357735 - LƯU KIM HOÀNG DUY', N'QuangVD', CAST(0x0B1F0B00 AS Date), null),
	(N'PS03251', N'TRẦN QUANG VŨ', CAST(0x4C240B00 AS Date), 1, N'0914531913', N'PS03251@fpt.edu.vn', N'0914531913 - TRẦN QUANG VŨ', N'QuangVD', CAST(0x4C240B00 AS Date), null),
	(N'PS03304', N'BÙI NGỌC THUẬN', CAST(0xFE1C0B00 AS Date), 1, N'0999794115', N'PS03304@fpt.edu.vn', N'0999794115 - BÙI NGỌC THUẬN', N'QuangVD', CAST(0xFE1C0B00 AS Date), null),
	(N'PS03306', N'HỒ VĂN HÀNH', CAST(0x06190B00 AS Date), 1, N'0912277868', N'PS03306@fpt.edu.vn', N'0912277868 - HỒ VĂN HÀNH', N'LongVH', CAST(0x06190B00 AS Date), null),
	(N'PS03308', N'TRẦN VIẾT HÙNG', CAST(0xD0220B00 AS Date), 1, N'0916050164', N'PS03308@fpt.edu.vn', N'0916050164 - TRẦN VIẾT HÙNG', N'QuangVD', CAST(0xD0220B00 AS Date), null),
	(N'PS03325', N'NGUYỄN HOÀNG MINH ĐỨC', CAST(0xAB1F0B00 AS Date), 1, N'0912234437', N'PS03325@fpt.edu.vn', N'0912234437 - NGUYỄN HOÀNG MINH ĐỨC', N'LongVH', CAST(0xAB1F0B00 AS Date), null),
	(N'PS03346', N'PHAN THANH PHONG', CAST(0xD7150B00 AS Date), 1, N'0937891282', N'PS03346@fpt.edu.vn', N'0937891282 - PHAN THANH PHONG', N'QuangVD', CAST(0xD7150B00 AS Date), null),
	(N'PS03383', N'TRẦN VŨ LUÂN', CAST(0x8E210B00 AS Date), 1, N'0962030316', N'PS03383@fpt.edu.vn', N'0962030316 - TRẦN VŨ LUÂN', N'QuangVD', CAST(0x8E210B00 AS Date), null),
	(N'PS03389', N'VŨ ĐỨC TUẤN', CAST(0x411A0B00 AS Date), 1, N'0911637415', N'PS03389@fpt.edu.vn', N'0911637415 - VŨ ĐỨC TUẤN', N'QuangVD', CAST(0x411A0B00 AS Date), null),
	(N'PS03410', N'TRẦN  NHẠT', CAST(0x3C190B00 AS Date), 1, N'0946219377', N'PS03410@fpt.edu.vn', N'0946219377 - TRẦN  NHẠT', N'LongVH', CAST(0x3C190B00 AS Date), null),
	(N'PS03411', N'TRƯƠNG THÀNH ĐẠT', CAST(0x3F1B0B00 AS Date), 1, N'0991509408', N'PS03411@fpt.edu.vn', N'0991509408 - TRƯƠNG THÀNH ĐẠT', N'LongVH', CAST(0x3F1B0B00 AS Date), null),
	(N'PS03425', N'TÔ VĂN NĂNG', CAST(0x6E190B00 AS Date), 1, N'0915134551', N'PS03425@fpt.edu.vn', N'0915134551 - TÔ VĂN NĂNG', N'QuangVD', CAST(0x6E190B00 AS Date), null),
	(N'PS03454', N'NGUYỄN NHẬT VĨNH', CAST(0x57230B00 AS Date), 1, N'0917886371', N'PS03454@fpt.edu.vn', N'0917886371 - NGUYỄN NHẬT VĨNH', N'LongVH', CAST(0x57230B00 AS Date), null),
	(N'PS03472', N'NGUYỄN VĂN HUY', CAST(0xD8150B00 AS Date), 1, N'0960620997', N'PS03472@fpt.edu.vn', N'0960620997 - NGUYỄN VĂN HUY', N'QuangVD', CAST(0xD8150B00 AS Date), null),
	(N'PS03488', N'NGUYỄN NHƯ NGỌC', CAST(0x651D0B00 AS Date), 0, N'0912880267', N'PS03488@fpt.edu.vn', N'0912880267 - NGUYỄN NHƯ NGỌC', N'LongVH', CAST(0x651D0B00 AS Date), null),
	(N'PS03530', N'PHẠM THÀNH TÂM', CAST(0x4D240B00 AS Date), 1, N'0918161783', N'PS03530@fpt.edu.vn', N'0918161783 - PHẠM THÀNH TÂM', N'QuangVD', CAST(0x4D240B00 AS Date), null),
	(N'PS03553', N'ĐINH TẤN CÔNG', CAST(0xEA240B00 AS Date), 1, N'0918182551', N'PS03553@fpt.edu.vn', N'0918182551 - ĐINH TẤN CÔNG', N'QuangVD', CAST(0xEA240B00 AS Date), null),
	(N'PS03561', N'LÊ MINH ĐIỀN', CAST(0xE91C0B00 AS Date), 1, N'0948199564', N'PS03561@fpt.edu.vn', N'0948199564 - LÊ MINH ĐIỀN', N'LongVH', CAST(0xE91C0B00 AS Date), null),
	(N'PS03596', N'NGUYỄN THANH HIỀN', CAST(0xED170B00 AS Date), 1, N'0910545901', N'PS03596@fpt.edu.vn', N'0910545901 - NGUYỄN THANH HIỀN', N'QuangVD', CAST(0xED170B00 AS Date), null),
	(N'PS03603', N'LÊ PHẠM KIM THANH', CAST(0x501C0B00 AS Date), 0, N'0924696779', N'PS03603@fpt.edu.vn', N'0924696779 - LÊ PHẠM KIM THANH', N'LongVH', CAST(0x501C0B00 AS Date), null),
	(N'PS03610', N'TRẦN ĐÌNH TRƯỜNG', CAST(0xF41C0B00 AS Date), 1, N'0941528106', N'PS03610@fpt.edu.vn', N'0941528106 - TRẦN ĐÌNH TRƯỜNG', N'QuangVD', CAST(0xF41C0B00 AS Date), null),
	(N'PS03614', N'NGUYỄN VĂN SÁU', CAST(0x37160B00 AS Date), 1, N'0940711328', N'PS03614@fpt.edu.vn', N'0940711328 - NGUYỄN VĂN SÁU', N'LongVH', CAST(0x37160B00 AS Date), null),
	(N'PS03618', N'PHÍ ĐÌNH VIỆT HÙNG', CAST(0xA91F0B00 AS Date), 1, N'0939020097', N'PS03618@fpt.edu.vn', N'0939020097 - PHÍ ĐÌNH VIỆT HÙNG', N'QuangVD', CAST(0xA91F0B00 AS Date), null),
	(N'PS03638', N'PHẠM NHẬT MINH', CAST(0x86200B00 AS Date), 1, N'0927771672', N'PS03638@fpt.edu.vn', N'0927771672 - PHẠM NHẬT MINH', N'QuangVD', CAST(0x86200B00 AS Date), null),
	(N'PS03640', N'LƯU THANH NGỌC', CAST(0x591B0B00 AS Date), 0, N'0918358164', N'PS03640@fpt.edu.vn', N'0918358164 - LƯU THANH NGỌC', N'LongVH', CAST(0x591B0B00 AS Date), null),
	(N'PS03662', N'NGUYỄN CAO NGỌC LỢI', CAST(0x34160B00 AS Date), 1, N'0930260679', N'PS03662@fpt.edu.vn', N'0930260679 - NGUYỄN CAO NGỌC LỢI', N'LongVH', CAST(0x34160B00 AS Date), null),
	(N'PS03674', N'TRẦN TUẤN ANH', CAST(0xF41E0B00 AS Date), 1, N'0914082094', N'PS03674@fpt.edu.vn', N'0914082094 - TRẦN TUẤN ANH', N'QuangVD', CAST(0xF41E0B00 AS Date), null);
go

set identity_insert khoahoc on;
go

insert into khoahoc (makhoahoc, machuyende, hocphi, thoiluong, ngaykhaigiang, ghichu, manhanvien, ngaytao) values
	(1, N'PRO02', 300, 90, CAST(0xBF3D0B00 AS Date), N'', N'QuangVD', CAST(0xB53D0B00 AS Date)),
	(2, N'PRO03', 300, 90, CAST(0xBF3D0B00 AS Date), N'', N'NgocTTM', CAST(0xB53D0B00 AS Date)),
	(3, N'RDB01', 100, 50, CAST(0xBF3D0B00 AS Date), N'', N'NgocTTM', CAST(0xB53D0B00 AS Date)),
	(4, N'JAV01', 250, 80, CAST(0xBF3D0B00 AS Date), N'', N'QuangVD', CAST(0xB53D0B00 AS Date));
go

set identity_insert khoahoc off;
go

set identity_insert hocvien on;
go

insert into hocvien (mahocvien, makhoahoc, manguoihoc, diemtrungbinh) values
	(1, 2, N'PS01638', -1),
	(2, 2, N'PS02037', -1),
	(3, 2, N'PS02771', -1),
	(5, 2, N'PS02930', -1),
	(7, 2, N'PS02983', -1),
	(8, 2, N'PS02988', -1),
	(11, 3, N'PS01638', -1),
	(12, 3, N'PS02037', -1),
	(13, 3, N'PS02771', -1),
	(14, 3, N'PS02867', -1),
	(15, 3, N'PS02930', -1),
	(16, 1, N'PS01638', 8),
	(17, 1, N'PS02037', 9),
	(19, 1, N'PS02867', 3),
	(20, 1, N'PS02930', 7),
	(33, 1, N'PS02771', 8),
	(34, 1, N'PS02979', 4),
	(35, 1, N'PS02983', 6),
	(36, 1, N'PS02988', -1),
	(37, 1, N'PS03031', -1),
	(38, 1, N'PS03046', -1),
	(39, 1, N'PS03080', -1),
	(40, 1, N'PS03088', -1),
	(41, 1, N'PS03096', -1),
	(42, 1, N'PS03104', -1),
	(43, 1, N'PS03120', -1),
	(44, 1, N'PS03130', -1);
go

set identity_insert hocvien off;
go

insert into nguoihoc_format
select *, format(ngaysinh, 'dd/MM/yyyy'), format(ngaydangky, 'dd/MM/yyyy') as ngaydk
from nguoihoc;
go

create trigger insert_nguoihoc_format
on nguoihoc
after insert
as
begin
    set nocount on;

    -- Chèn dữ liệu vào bảng nguoihoc_format từ bảng nguoihoc
    INSERT INTO nguoihoc_format
    SELECT *, format(ngaysinh, 'dd/MM/yyyy'), format(ngaydangky, 'dd/MM/yyyy') as ngaydk
    FROM inserted;
END;
go