<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="reservation">
        <div class="calendar">
            <input type="date" v-model="calendarDate" @change="changeCalendar">
            <div class="header">
                <button class="prev" @click="switchMonth('prev')">◀</button>
                <p>{{ year }}年{{ month }}月</p>
                <button class="next" @click="switchMonth('next')">▶</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>日</th>
                        <th>月</th>
                        <th>火</th>
                        <th>水</th>
                        <th>木</th>
                        <th>金</th>
                        <th>土</th>
                    </tr>
                </thead>
                <tr v-for="(item, index) in dayArray" :key="index">
                    <td v-for="(i, index) in item" :key="index"
                        :class="{ not_this_moon: !i.this_month, today: i.is_today, not_bookable: !i.bookable }">
                        <p>{{ i.date }}</p>
                        <div class="countGuest" :class="{ hasGuest: i.countGuest !== 0 }"
                            @click="getReservationDataByDay(i.date)">
                            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24">
                                <path fill="#818181"
                                    d="M24 14.6c0 .6-1.2 1-2.6 1.2c-.9-1.7-2.7-3-4.8-3.9c.2-.3.4-.5.6-.8h.8c3.1-.1 6 1.8 6 3.5M6.8 11H6c-3.1 0-6 1.9-6 3.6c0 .6 1.2 1 2.6 1.2c.9-1.7 2.7-3 4.8-3.9zm5.2 1c2.2 0 4-1.8 4-4s-1.8-4-4-4s-4 1.8-4 4s1.8 4 4 4m0 1c-4.1 0-8 2.6-8 5c0 2 8 2 8 2s8 0 8-2c0-2.4-3.9-5-8-5m5.7-3h.3c1.7 0 3-1.3 3-3s-1.3-3-3-3c-.5 0-.9.1-1.3.3c.8 1 1.3 2.3 1.3 3.7c0 .7-.1 1.4-.3 2M6 10h.3C6.1 9.4 6 8.7 6 8c0-1.4.5-2.7 1.3-3.7C6.9 4.1 6.5 4 6 4C4.3 4 3 5.3 3 7s1.3 3 3 3" />
                            </svg>
                            <p>{{ i.countGuest }}</p>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="info">
            <div class="header">
                <p>予約詳細</p>
            </div>
            <div class="content">
                <h4 v-if="oneDayReservationData.length !== 0">{{ dateText }}</h4>
                <ul>
                    <li v-for="(item, index) in oneDayReservationData" :key="index">
                        <select class="guestState" v-model="item.guestState"
                            @change="seed_changeVisitStatus(item.id, item.guestState)">
                            <option value="0">未来店</option>
                            <option value="1">来店済</option>
                            <option value="2">キャンセル</option>
                        </select>
                        <div class="tag">組{{ index + 1 }}</div>
                        <p>担当者名：{{ item.name }}({{ item.katakana }})</p>
                        <p>予約時間帯：{{ item.timeRange }}</p>
                        <p>予約内容：{{ item.content }}</p>
                        <p>人数：{{ item.people }}</p>
                        <p>連絡先：{{ item.tel }}</p>
                        <p>メールアドレス：{{ item.mail }}</p>
                        <p>特記事項：{{ item.specialNote }}</p>
                        <p>来店状態：{{ getGuestStateText(item.guestState) }}</p>
                        <div v-if="canReserveTable(item.date, item.timeRange)" class="deskSelect">
                            <p>テーブル指定：</p>
                            <select v-model="selectedTable">
                                <option value="">--テーブル番号--</option>
                                <option v-for="(item, index) in item.availableTableArray" :key="index" :value="item">
                                    {{ item }}
                                </option>
                            </select>
                            <button @click="seed_addReservedTableId(item.id, item.date)">指定</button>
                        </div>
                        <div v-if="canReserveTable(item.date, item.timeRange)" class="SelectedDeskes">
                            <p>指定されたテーブル: </p>
                            <div v-for="(deskId, index) in item.reservedTablesArray" :key="index">
                                <p>{{ deskId }}</p>
                                <button @click="seed_deleteSelectedTableById(item.id, deskId, item.date)">×</button>
                            </div>
                        </div>
                        <div v-else="canReserveTable(item.date, item.timeRange)" class="SelectedDeskes">
                            <p>指定されたテーブル: {{ item.reservedTables }} </p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="qrCode" @click="expandQrCode" :class="{ expand: isExpand }">
            <div><img src="@/assets/images/qrCode/reservation_qrCode.png"></div>
            <P>予約QRコード</P>
        </div>
    </section>
</template>

<script setup>
import { ref, onMounted, reactive, toRaw } from "vue"


/*************************************
* ページの初期化時に実行
**************************************/
onMounted(async () => {
    seed_fetchAllTables()
    const countGuestArray = await seed_fetchReservationDataByMouth(year.value, month.value); // 解析 Promise
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value, countGuestArray);
});


// 現在の年と月を使用
const today = new Date();
const this_year = today.getFullYear();
const this_month = today.getMonth();
const year = ref(this_year)
const month = ref(this_month + 1)
const dayArray = ref()

// 指定された年、月のカレンダーに前後の週を含めた日付リストを生成する関数
const getDaysOfMonthWithSurroundingWeeks = (year, month, countGuestArray) => {
    const daysOfMonth = [];
    let daysOfWeek = [];

    // 辅助函数：将日期加入到当前周或完成一周时推入结果数组
    const addDayToWeek = (dayInfo) => {
        daysOfWeek.push(dayInfo);
        if (daysOfWeek.length === 7) {
            daysOfMonth.push([...daysOfWeek]);
            daysOfWeek = [];
        }
    };

    // 处理月份溢出
    const adjustDate = (year, month) => {
        if (month < 1) {
            return { year: year - 1, month: 12 };
        } else if (month > 12) {
            return { year: year + 1, month: 1 };
        }
        return { year, month };
    };

    // 前一个月的最后一周
    const { year: prevYear, month: prevMonth } = adjustDate(year, month - 1);
    const previousMonthLastDate = new Date(prevYear, prevMonth, 0);
    const lastDayOfPrevMonth = previousMonthLastDate.getDate();
    const lastWeekStart = lastDayOfPrevMonth - previousMonthLastDate.getDay();
    for (let day = lastWeekStart; day <= lastDayOfPrevMonth; day++) {
        const dayDate = new Date(prevYear, prevMonth - 1, day);
        addDayToWeek({
            year: dayDate.getFullYear(),
            month: dayDate.getMonth() + 1,
            date: dayDate.getDate(),
            weekDay: dayDate.getDay(),
            this_month: false,
            isToday: false,
        });
    }

    // 当前月所有日期
    const currentMonthDate = new Date(year, month - 1, 1);
    const today = new Date(); // 获取当前日期

    while (currentMonthDate.getMonth() === month - 1) {
        // console.log(currentMonthDate);

        // 提前提取当前日期信息
        const currentYear = currentMonthDate.getFullYear();
        const currentMonth = currentMonthDate.getMonth();
        const currentDate = currentMonthDate.getDate();

        // 检查是否为当前日期
        const isToday =
            currentYear === today.getFullYear() &&
            currentMonth === today.getMonth() &&
            currentDate === today.getDate();

        // 检查是否可预约
        const bookable =
            currentYear > today.getFullYear() ||
            (currentYear === today.getFullYear() &&
                (currentMonth > today.getMonth() ||
                    (currentMonth === today.getMonth() && currentDate >= today.getDate())));

        //予約人数をチェックする
        const countGuest = String(currentMonthDate.getDate()) in countGuestArray ? countGuestArray[String(currentMonthDate.getDate())] : 0

        addDayToWeek({
            year: currentMonthDate.getFullYear(),
            month: currentMonthDate.getMonth() + 1,
            date: currentMonthDate.getDate(),
            weekDay: currentMonthDate.getDay(),
            this_month: true,
            is_today: isToday, // 添加标识
            bookable: bookable,
            countGuest: countGuest
        });

        currentMonthDate.setDate(currentMonthDate.getDate() + 1);
    }

    // 下一个月的第一周
    const { year: nextYear, month: nextMonth } = adjustDate(year, month + 1);
    const nextMonthStartDate = new Date(nextYear, nextMonth - 1, 1);
    while (daysOfWeek.length > 0 || nextMonthStartDate.getDay() !== 0) {
        addDayToWeek({
            year: nextMonthStartDate.getFullYear(),
            month: nextMonthStartDate.getMonth() + 1,
            date: nextMonthStartDate.getDate(),
            weekDay: nextMonthStartDate.getDay(),
            this_month: false,
            isToday: false,
        });
        nextMonthStartDate.setDate(nextMonthStartDate.getDate() + 1);
    }

    return daysOfMonth;
};

const switchMonth = async (direction) => {
    if (direction === "prev") {
        month.value = month.value > 1 ? month.value - 1 : 12;
        year.value = month.value === 12 ? year.value - 1 : year.value;
    } else if (direction === "next") {
        month.value = month.value < 12 ? month.value + 1 : 1;
        year.value = month.value === 1 ? year.value + 1 : year.value;
    }
    calendarDate.value = `${year.value}-${String(month.value).padStart(2, '0')}-01`
    const countGuestArray = await seed_fetchReservationDataByMouth(year.value, month.value); // 解析 Promise
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value, countGuestArray);
}


const calendarDate = ref(today.toISOString().split("T")[0])

const changeCalendar = async () => {
    const assignDate = new Date(calendarDate.value);
    const assignMonth = assignDate.getMonth() + 1;
    const assignYear = assignDate.getFullYear();
    month.value = assignMonth
    year.value = assignYear
    const countGuestArray = await seed_fetchReservationDataByMouth(year.value, month.value); // 解析 Promise
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value, countGuestArray);
}

const isExpand = ref(false)

const expandQrCode = () => {
    isExpand.value = !isExpand.value
}


//予約情報をサーバから所得する
import { fetchReservationDataByMouth } from "@/api/reservationApi";
const reservationData = ref("")
const seed_fetchReservationDataByMouth = async () => {
    try {
        const res = await fetchReservationDataByMouth({ date: calendarDate.value });
        if (res.data.code !== 0) {
            reservationData.value = res.data.data
            console.log(reservationData.value);

            const countGuestArray = countGuestGroupsByDay(res.data.data)
            processReservationData()
            return countGuestArray
        } else {
            console.log("送信失敗しました:" + res.data.msg)
            return false
        }
    } catch (err) {
        console.log("リクエストエラー:", err)
        return false;
    }
}

// 予約情報データを加工する
const processReservationData = () => {
  reservationData.value.forEach(item => {
    const reservedTables = item.reservedTables;

    // 文字列を配列に分割する
    const reservedTablesArray = reservedTables ? reservedTables.split(",") : [];

    // 元データに配列を保存する
    item.reservedTablesArray = reservedTablesArray;

    // すでに予約されたテーブルを除外して、利用可能なテーブルを取得する
    item.availableTableArray = availableTableArray.value.filter(
      id => !reservedTablesArray.includes(id)
    );
  });
};

//指定された日数内のゲストグループ数を計算する
const countGuestGroupsByDay = (res) => {
    const countGuestArray = {}
    res.forEach(item => {
        const dateObj = new Date(item.date);
        const day = dateObj.getDate();
        if (countGuestArray[day]) {
            countGuestArray[day] += 1
        } else {
            countGuestArray[day] = 1
        }
    });
    return countGuestArray;
}

//指定された日数の予約情報を取得する
const oneDayReservationData = ref([])
const dateText = ref("")

const getReservationDataByDay = (day) => {
    oneDayReservationData.value = []
    reservationData.value.forEach(item => {
        const dateObj = new Date(item.date);
        const dayNum = dateObj.getDate();
        if (day === dayNum) {
            dateText.value = item.date
            oneDayReservationData.value.push(item)
        }
    })
}

/**************************************************************************
* 時間を判断する関数で、判断結果に応じてテーブルの指定可否を決定する。
***************************************************************************/
const canReserveTable = (dateStr, timeRangeStr) => {
    // 現在の時刻を取得
    const now = new Date();

    // 日付オブジェクトを作成
    const baseDate = new Date(dateStr);

    // 時間範囲を分割して、開始・終了時間を取得
    const startTime = timeRangeStr.split("~")[0];

    // 開始時間と終了時間の Date オブジェクトを作成
    const startDateTime = new Date(baseDate);


    // 時間と分を設定
    const [startHour, startMinute] = startTime.split(":").map(Number);

    startDateTime.setHours(startHour, startMinute, 0, 0);

    // 現在の時間と比較
    return now < startDateTime;
}

/*************************************
* テーブルをすべてフェッチ
**************************************/
import { fetchAllTables } from "@/api/deskApi";
const seed_fetchAllTables = async () => {
    try {
        const res = await fetchAllTables();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            getAvailableArray(res.data.data)
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

/*************************************
* テーブルデータを、予約可能なテーブルと予約済みのテーブルの2つの配列に分割する。
**************************************/
const availableTableArray = ref([])
// const reservedTableAraay = ref([])
const getAvailableArray = (tableData) => {
    availableTableArray.value = []
    tableData.forEach((item) => {
        if (item.deskState !== '3') {
            availableTableArray.value.push(item.id)
            console.log(availableTableArray.value);
        }
    })
}

/*************************************
* 予約データに予約済みのテーブルIDを追加する。
**************************************/
import { addReservedTableId } from "@/api/reservationApi";
const selectedTable = ref("")
// const reservedDeskArray = ref([])
const seed_addReservedTableId = async (reservationId, date) => {
    try {
        const res = await addReservedTableId({ reservationId: reservationId, deskId: selectedTable.value });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            const day = Number(date.split("-")[2])
            await seed_fetchReservationDataByMouth(); // 等待数据更新
            getReservationDataByDay(day) // 再重新刷新当天数据
            selectedTable.value = ""
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}


/*************************************
*予約済みテーブルを削除
**************************************/
import { deleteSelectedTableById } from "@/api/reservationApi";
const seed_deleteSelectedTableById = async (reservationId, deskId, date) => {
    try {
        const res = await deleteSelectedTableById({ reservationId, deskId });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            const day = Number(date.split("-")[2])
            await seed_fetchReservationDataByMouth(); // 等待数据更新
            getReservationDataByDay(day) // 再重新刷新当天数据
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

/*************************************
* 来店状態コードをメッセージに変換する
**************************************/
const getGuestStateText = (code) => {
    switch (code) {
        case "0":
            return "未来店"
        case "1":
            return "来店済"
        case "2":
            return "キャンセル"
    }
}

/*************************************
*　予約データの来店状態の変更する。
**************************************/
import { changeVisitStatus } from "@/api/reservationApi";
const seed_changeVisitStatus = async (reservationId, guestState) => {
    try {
        const res = await changeVisitStatus({ reservationId, guestState });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            // const day = Number(date.split("-")[2])
            // await seed_fetchReservationDataBychangeVisitStatusMouth(); // 等待数据更新
            // getReservationDataByDay(day) // 再重新刷新当天数据
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

</script>

<style lang="less" scoped>
@import '@/assets/css/reservation.less';
</style>