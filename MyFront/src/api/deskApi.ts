import instance from './index';

/**
 * テーブルをフェッチ
 */
export function fetchAllTables() {
    return instance.get('/api/desk');
}
