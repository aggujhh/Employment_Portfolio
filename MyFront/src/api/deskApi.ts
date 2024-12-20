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