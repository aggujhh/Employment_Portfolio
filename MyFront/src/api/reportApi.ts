import instance from './index';

/**
 * 指定した日数分のレポートデータを取得する関数
 */
export function fetchReportDataByCountDays(data: { countDays: number }) {
    return instance.get('/api/report', {
        params: data
    });
}