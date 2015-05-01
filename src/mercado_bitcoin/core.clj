(ns mercado-bitcoin.core
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as str]
   [clj-time
    [coerce :as c]
    [core :as time]]
   [pandect.core :refer :all])
  (:import [org.jsoup Jsoup]))


(def trade-address "https://www.mercadobitcoin.com.br/tapi/")

(def from-seconds-ts
  #(c/from-long (* 1000 %)))

(def to-seconds-ts
  #(int (/ (c/to-long %) 1000)))

(defn info [address]
  (let [ans (-> (client/get address {:retry-handler (fn [ex try-count http-context]
                                                      (Thread/sleep 3000)
                                                      (if (> try-count 4) false true))})
                :body
                (json/read-str :key-fn keyword
                               :value-fn #(if (= :date %1)
                                            (from-seconds-ts %2)
                                            %2)))]
    ans))

(defn- wrap-credentials-http-request [tapi-codigo tapi-chave PIN]
  (let [sign #(sha512-hmac % tapi-codigo)]
    (fn [method params]
      (let [tonce (to-seconds-ts (time/now))
            params (merge params {:method method :tonce tonce})
            sign (sign (str method ":" PIN ":" tonce))
            header {"Content-type" "application/x-www-form-urlencoded"
                    "Key" tapi-chave
                    "Sign" sign}]
        (client/post trade-address
                     {:headers header
                      :form-params params})))))

(defn- answer-parser [key value]
  (case key
    :date (from-seconds-ts value)
    :created (from-seconds-ts (Long/parseLong value))
    :price (if (number? value) value (Double/parseDouble value))
    :volume (if (number? value) value (Double/parseDouble value))
    :rate (if (number? value) value (Double/parseDouble value))
    value))

(defn get-trader [tapi-codigo tapi-chave PIN]
  (let [trader (wrap-credentials-http-request tapi-codigo tapi-chave PIN)]
    (fn
      ([method & {:as params}]
         ;;remember the Tonce problem >: /
         (-> (trader method params)
             :body
             (json/read-str :key-fn keyword :value-fn answer-parser))))))

