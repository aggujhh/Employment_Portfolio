// import instance from './index';

import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'https://www.cyg1995.xyz', // 基本URL
    timeout: 10000, // タイムアウト時間（ミリ秒）
    headers: { 'Content-Type': 'application/json' }, // デフォルトのヘッダー
    withCredentials: true,
});

/**
 * 予約項目をサーバに送信する関数
 */
export function addReservation(data: {
    date: String,
    timeRange: String,
    name: String,
    katakana: String,
    content: String,
    people: Number,
    tel: Number,
    mail: String,
    specialNote: String,
}) {
    return axiosInstance.post('/api/reservation', data);
}