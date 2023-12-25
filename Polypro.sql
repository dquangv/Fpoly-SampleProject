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
	ngaydangky date default getdate()
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
	vaitro bit default 0
);
go

alter table nguoihoc
add
	constraint fk_nh_nv
	foreign key (manhanvien)
	references nhanvien (manhanvien);
go

alter table khoahoc
add 
	constraint fk_kh_cd
	foreign key (machuyende)
	references chuyende (machuyende) on update cascade,
	
	constraint fk_kh_nv
	foreign key (manhanvien)
	references nhanvien (manhanvien) on update cascade;
go

alter table hocvien
add
	constraint fk_hv_kh
	foreign key (makhoahoc)
	references khoahoc (makhoahoc) on delete cascade,
	
	constraint fk_hv_nh
	foreign key (manguoihoc)
	references nguoihoc (manguoihoc) on update cascade;
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
	group by ngaydangky
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