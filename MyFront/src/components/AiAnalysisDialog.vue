<template>
    <section id='aiAnalysis' v-if="isVisible">
        <div class="dialog">
            <h3>統計データを基にAIの分析を行い、アドバイスを提供します。</h3>
            <button class="close_btn" @click="close">×</button>
            <div class="img"></div>
            <div class="text" v-html="renderedMarkdown"></div>
        </div>
    </section>
</template>

<script setup>
import { onMounted, ref, watch } from "vue"


onMounted(() => {
    googleGeminiAiRun();
});

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'reportData'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}

watch(
    () => props.reportData,
    (newValue) => {
        if (newValue) {
            googleGeminiAiRun(newValue);
        }
    }
);

/*************************************
*GoogleGeminiAI
**************************************/
import { GoogleGenerativeAI } from '@google/generative-ai';
import { marked } from 'marked';

const geminiText = ref("")
const renderedMarkdown = ref("");
// Access your API key as an environment variable (see "Set up your API key" above)
const genAI = new GoogleGenerativeAI(import.meta.env.VITE_API_KEY);

async function googleGeminiAiRun(data) {
    // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
    const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });



    const prompt = "以下の統計データを基に、AI分析を行い、店舗運営改善の具体的なアドバイスを簡潔に提案してください。\n" +
        "なお、データは今年のものです。月ごとのデータは今月のものであり、一部未到達のためゼロで表示されています。\n" +
        JSON.stringify(data, null, 2);
    console.log(prompt);
    // 调用生成流式 API
    const result = await model.generateContentStream(prompt);
    // 清空文本
    geminiText.value = "";
    marked.setOptions({
        breaks: true, // 将换行符视为 <br>
    });
    // 逐步处理生成的文本流
    for await (const chunk of result.stream) {
        let chunkText = chunk.text(); // 获取当前流块的文本
        geminiText.value += chunkText; // 追加到显示变量
        renderedMarkdown.value = marked(geminiText.value);
    }
}



</script>

<style lang="less" scoped>
@import '@/assets/css/aiAnalysisDialog.less';
</style>
