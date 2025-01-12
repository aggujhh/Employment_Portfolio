import instance from './index';

/**
 * テーブルをすべてフェッチ
 */
export function fetchAllTables() {
    return instance.get('/api/desk');
}

/**
 * テーブルをすべて最初の状態を戻る
 */
export function resetAllTables() {
    return instance.put('/api/desk');
}

/**
 * テーブルを一時的に停止に設定
 */
export function disableDeskById(data: { id: string }) {
    return instance.patch('/api/desk/disable', data);
}

/**
 * テーブルを再利用に設定
 */
export function restoreDeskById(data: { id: string }) {
    return instance.patch('/api/desk/restore', data);
}
