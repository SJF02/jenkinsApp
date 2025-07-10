import React, { useState, useEffect } from "react";
import { SERVER_URL } from "./constants";
import { DataGrid } from '@mui/x-data-grid';

const CarList = () => {
  const [cars, setCars] = useState([]); // car목록을 서버로부터 가져와서 저장

  // Application이 시작되면 처음에 1번만 요청
  useEffect(() => {
    fetchCars();
  }, []);

  const fetchCars = () => {
    fetch(SERVER_URL + "api/cars") // get 요청
      .then((resp) => resp.json()) // 수신데이터 json 추출
      .then((data) => setCars(data._embedded.cars)) // json에서 cars 추출
      .catch((e) => console.log(e)); // 통신 오류 시 예외 출력
  };

  // 삭제 요청이 정상 처리되었다면, 서버로 다시 Car 리스트 요청
  const onDelClick = (url)=>{
    fetch(url, {method:"DELETE"})
    .then((resp)=>fetchCars())
    .catch((e)=>console.error(e));
  }

  // x-Data-Grid의 컬럼으로 사용할 정보
  // field가 json객체의 필드명칭과 동일해야 한다.
  const columns = [
    { field: "brand", headerName: "Brand", width: 200 },
    { field: "model", headerName: "Model", width: 200 },
    { field: "color", headerName: "Color", width: 200 },
    { field: "registrationNumber", headerName: "regNum", width: 200 },
    { field: "modelYear", headerName: "Year", width: 150 },
    { field: "price", headerName: "Price", width: 150 },
    {
      field: "_links.self.href",
      headerName: "",
      sortable: false,
      filterable: false,
      renderCell: (row)=>{
        // row.id === getRowId={(row) => row._links.self.href}
        return <button onClick={()=>onDelClick(row.id)}>Delete</button>
      }
    }
  ];

  return (
    <div style={{ height: 500, width: "100%" }}>
      <DataGrid
        columns={columns}
        rows={cars}
        getRowId={(row) => row._links.self.href}
      />
    </div>
  );
};

export default CarList;
