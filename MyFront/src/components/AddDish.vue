<template>
    <section id='add_dish' v-if="isVisible">
        <div class="dialog">
            <h3>料理を追加する?</h3>
            <ul>
                <li>
                    <label>
                        <p>料理名</p>
                        <input type="text" v-model="req.name" />
                    </label>
                </li>
                <li>
                    <label>
                        <p>値段</p>
                        <input type="text" v-model="req.price" />
                        <span>円</span>
                    </label>
                </li>
                <li>
                    <p>販売状況</p>
                    <div class="radio">
                        <label><input type="radio" name="status" value="0" v-model="req.state" />販売中
                        </label>
                        <label><input type="radio" name="status" value="1" v-model="req.state" />一時停止中
                        </label>
                    </div>
                </li>
                <li>
                    <p>画像</p>
                    <input type="file" @change="onImageChange" id="upImg"/>
                    <label for="upImg">
                        <div class="dishImg" :style="`background-image: url(${req.image})`"></div>
                        <div>UPLORD</div>
                    </label>
                </li>
                <li>
                    <p>料理の紹介</p>
                    <textarea v-model="req.description"></textarea>
                </li>
            </ul>
            <div>
                <button @click="agree()">確認</button>
                <button class="close-btn" @click="close()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, reactive } from "vue"
defineProps(['isVisible'])
const emit = defineEmits(['close']);

const defaultReq = {
    name: "",
    image: "",
    price: "",
    state: "0",
    description: "",
};
const req = reactive({ ...defaultReq })

const onImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            req.image = e.target.result;
        };
        reader.readAsDataURL(file);
    }
}



const close = () => {
    Object.assign(req, defaultReq);
    emit('close');
}

</script>

<style lang="less" scoped>
@import '@/assets/css/add_dish.less';
</style>
