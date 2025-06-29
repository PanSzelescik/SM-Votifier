package pl.ibcgames.smvotifier.response;

public record GetPluginDetailsResponse(long responseCachedAt,
                                       boolean isPromotionActive,
                                       long promotionExpireAt,
                                       int votesCount,
                                       long votesCachedAt) {
}
