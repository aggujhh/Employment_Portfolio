import instance from './index';
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'https://www.cyg1995.xyz', // 基本URL
    timeout: 10000, // タイムアウト時間（ミリ秒）
    headers: { 'Content-Type': 'application/json' }, // デフォルトのヘッダー
    // withCredentials: true,
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

/**
 * 指定した月の予約データを取得する関数
 */
export function fetchReservationDataByMouth(data: { date: String }) {
    return instance.get('/api/reservation', {
        params: data
    });
}


/**
 * 予約データに予約済みのテーブルIDを追加する。
 */
export function addReservedTableId(data: { reservationId: Number, deskId: string }) {
    return instance.patch('/api/reservation', data);
}


/**
 * IDにより、予約済みテーブルんの予約状態を削除
 */
export function deleteSelectedTableById(data: { reservationId: Number, deskId: string }) {
    return instance.delete('/api/reservation', {
        data: data
    });
}

/**
 * 予約データの来店状態の変更する
 */
export function changeVisitStatus(data: { reservationId: Number, guestState: string }) {
    return instance.patch('/api/reservation/guestState', data);
}
