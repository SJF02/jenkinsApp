import React, { useState, useEffect } from "react";
import { SERVER_URL } from "./constants";

const CarList = () => {
    const [cars, setCars] = useState([]);   // car목록을 서버로부터 가져와서 저장

    // Application이 시작되면 처음에 1번만 요청
    useEffect(()=>{
        fetch(SERVER_URL + "api/cars")     // get 요청
        .then((resp)=>resp.json())                  // 수신데이터 json 추출
        .then((data)=>setCars(data._embedded.cars)) // json에서 cars 추출
        .catch((e)=>console.log(e));                // 통신 오류 시 예외 출력
    }, []);

    return (
        <div>
            <table>
                <tbody>
                    {cars.map((car, index)=>{
                        return (
                            <tr key={index}>
                                <td>{car.brand}</td>
                                <td>{car.model}</td>
                                <td>{car.color}</td>
                                <td>{car.year}</td>
                                <td>{car.price}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    )
}

export default CarList;